package com.example.newsService.configuration.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
@Profile("prod")
public class AppCacheProperties {

    private List<String> cacheNames = new ArrayList<>();

    private Map<String, CacheProperties> caches = new HashMap<>();

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }


    public interface CacheNames {

        String NEWS_BY_ID = "newsById";
        String NEWS = "news";
        String NEWS_BY_USERNAME_AND_CATEGORY_ID = "newsByUsernameAndCategoryId";
        String USERS = "users";
        String USER_BY_ID = "userById";
        String COMMENTS_BY_NEWS_ID = "commentsByNewsId";
        String COMMENT_BY_ID = "commentById";
        String NEWS_CATEGORIES = "newsCategories";
        String NEWS_CATEGORY_BY_ID = "newsCategoryById";
    }

}


