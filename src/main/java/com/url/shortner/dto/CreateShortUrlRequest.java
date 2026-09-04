package com.url.shortner.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
        @NotBlank(message = "URL is required")
        @URL(message = "Please provide a valid URL")
        String url
) {
}
