package com.url.shortner.service;

import com.url.shortner.entity.ShortUrl;
import com.url.shortner.exception.ShortUrlNotFoundException;
import com.url.shortner.repository.ShortUrlRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 7;
    private static final int MAX_GENERATION_ATTEMPTS = 10;

    private final ShortUrlRepository shortUrlRepository;
    private final SecureRandom random = new SecureRandom();

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Override
    public ShortUrl createShortUrl(String originalUrl) {
        for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
            String shortCode = generateShortCode();
            if (shortUrlRepository.existsByShortCode(shortCode)) {
                continue;
            }
            try {
                return shortUrlRepository.saveAndFlush(new ShortUrl(originalUrl, shortCode));
            } catch (DataIntegrityViolationException ignored) {
                // Unique constraint hit from a concurrent insert; try another code.
            }
        }
        throw new IllegalStateException("Could not generate a unique short URL. Please try again.");
    }

    @Override
    @Transactional
    public ShortUrl resolveAndTrack(String shortCode) {
        ShortUrl shortUrl = getByShortCode(shortCode);
        shortUrl.registerClick();
        return shortUrl;
    }

    @Override
    @Transactional(readOnly = true)
    public ShortUrl getByShortCode(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
    }

    private String generateShortCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }
}
