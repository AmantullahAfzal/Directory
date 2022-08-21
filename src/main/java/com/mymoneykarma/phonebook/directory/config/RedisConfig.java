package com.mymoneykarma.phonebook.directory.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;


@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig {

    @Autowired
    private CacheManager cacheManager;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        RedisCacheConfiguration CacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        CacheConfig.disableCachingNullValues();
        Map<String, RedisCacheConfiguration> cacheConfigurations =
                new HashMap<>();

        cacheConfigurations.put(
                "apiHitCount", CacheConfig.
                        entryTtl(Duration.ofHours(24)).
                        serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).
                        serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))

        );

        cacheConfigurations.put(
                "stopMessage", CacheConfig.
                        entryTtl(Duration.ofHours(4)).
                        serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).
                        serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
        );
        return RedisCacheManager.builder(factory).
                cacheDefaults(CacheConfig).
                withInitialCacheConfigurations(cacheConfigurations).
                build();
    }

}
