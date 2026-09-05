package com.url.shortner.controller;

import com.url.shortner.dto.CreateShortUrlRequest;
import com.url.shortner.dto.ShortUrlResponse;
import com.url.shortner.entity.ShortUrl;
import com.url.shortner.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final String publicBaseUrl;

    public ShortUrlController(
            ShortUrlService shortUrlService,
            @Value("${app.public-base-url}") String publicBaseUrl
    ) {
        this.shortUrlService = shortUrlService;
        this.publicBaseUrl = trimTrailingSlash(publicBaseUrl);
    }

    @PostMapping("/api/urls")
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        ShortUrl shortUrl = shortUrlService.createShortUrl(request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(shortUrl));
    }

    @GetMapping("/api/urls/{shortCode}")
    public ShortUrlResponse getUrlDetails(@PathVariable String shortCode) {
        return toResponse(shortUrlService.getByShortCode(shortCode));
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]{7}}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        ShortUrl shortUrl = shortUrlService.resolveAndTrack(shortCode);
        String destination = shortUrl.getOriginalUrl();
        if (!isHttpUrl(destination)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, destination)
                .build();
    }

    private ShortUrlResponse toResponse(ShortUrl shortUrl) {
        return new ShortUrlResponse(
                shortUrl.getOriginalUrl(),
                shortUrl.getShortCode(),
                publicBaseUrl + "/" + shortUrl.getShortCode(),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }

    private static String trimTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }

    private static boolean isHttpUrl(String value) {
        if (value == null) {
            return false;
        }
        String normalized = value.trim().toLowerCase();
        return normalized.startsWith("https://") || normalized.startsWith("http://");
    }
}
