package com.url.shortner.dto;

import java.time.LocalDateTime;

public record ShortUrlResponse(
        String originalUrl,
        String shortCode,
        String shortUrl,
        long clickCount,
        LocalDateTime createdAt
) {
}
