package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.News;
import com.example.newsService.web.model.filters.NewsFilter;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilter newsFilter) {
        return Specification.where(byNewsCategoryId(newsFilter.getNewsCategoryId())).
                and(byUserId(newsFilter.getUserId()));
    }


    static Specification<News> byUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if(userId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    static Specification<News> byNewsCategoryId(Long newsCategoryId) {
        return (root, query, criteriaBuilder) -> {
            if(newsCategoryId == null) {
            return null;
            }

            return criteriaBuilder.equal(root.get("newsCategory").get("id"), newsCategoryId);

        };
    }
}
