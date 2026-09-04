package com.url.shortner.service;

import com.url.shortner.entity.ShortUrl;

public interface ShortUrlService {

    ShortUrl createShortUrl(String originalUrl);

    ShortUrl resolveAndTrack(String shortCode);

    ShortUrl getByShortCode(String shortCode);
}
