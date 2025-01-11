package com.example.newsService.configuration.cache;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@EnableConfigurationProperties(AppCacheProperties.class)
@Profile("prod")
public class CacheConfiguration {



    @Bean
    public CacheManager redisCacheManager(AppCacheProperties appProperties
            , LettuceConnectionFactory lettuceConnectionFactory) {
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> redisCacheConfiguration = new HashMap<>();

        appProperties.getCacheNames().forEach(name ->
                redisCacheConfiguration.put(name, RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(appProperties.getCaches().get(name).getExpiry())));

        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(redisCacheConfiguration)
                .build();

    }

}
