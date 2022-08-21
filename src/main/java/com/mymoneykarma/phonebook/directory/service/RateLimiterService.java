package com.mymoneykarma.phonebook.directory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "API")
public class RateLimiterService {
    @Autowired
    RedisTemplate<String, Object> template;

    @Cacheable(cacheNames = "apiHitCount", key ="{#userId}" )
    public String getApiHitCount(Integer userId) {
        return "0";
    }

    public void incrementApiHitCount(Integer userId) {
        template.
                opsForValue().
                increment("apiHitCount" + "::" + userId);
    }
}