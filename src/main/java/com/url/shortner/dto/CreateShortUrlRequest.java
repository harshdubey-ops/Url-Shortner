package com.url.shortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
        @NotBlank(message = "URL is required")
        @Size(max = 2048, message = "URL is too long")
        @Pattern(
                regexp = "^https?://[^\\s]+$",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Please provide a valid http or https URL"
        )
        String url
) {
}
