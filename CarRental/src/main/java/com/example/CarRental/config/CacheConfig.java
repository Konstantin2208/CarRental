package com.example.CarRental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Bean
    public org.springframework.cache.CacheManager cacheManager() {
        return new org.springframework.cache.concurrent.ConcurrentMapCacheManager(
                "cars",
                "carDetails"
        );
    }
}

