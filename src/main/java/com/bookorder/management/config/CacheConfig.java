package com.bookorder.management.config;

import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {


    public static final String BOOK_CACHE = "BOOK_CACHE";

    @Value("${spring.cache.caffeine.spec}")
    private String cacheSpec;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(BOOK_CACHE);
        cacheManager.setAllowNullValues(false);
        cacheManager.setCaffeineSpec(caffeineSpec());
        return cacheManager;
    }

    CaffeineSpec caffeineSpec() {
        return CaffeineSpec.parse(cacheSpec);
    }

}
