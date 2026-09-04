package com.url.shortner.controller;

import com.url.shortner.dto.CreateShortUrlRequest;
import com.url.shortner.dto.ShortUrlResponse;
import com.url.shortner.entity.ShortUrl;
import com.url.shortner.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
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

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        ShortUrl shortUrl = shortUrlService.resolveAndTrack(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl())
                .build();
    }

    private ShortUrlResponse toResponse(ShortUrl shortUrl) {
        String shortUrlValue = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{shortCode}")
                .buildAndExpand(shortUrl.getShortCode())
                .toUriString();

        return new ShortUrlResponse(
                shortUrl.getOriginalUrl(),
                shortUrl.getShortCode(),
                shortUrlValue,
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }
}
