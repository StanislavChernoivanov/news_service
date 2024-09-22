package com.example.newsService.model.repositories.specifications;

import com.example.newsService.model.entities.NewsCategory;
import org.springframework.data.jpa.domain.Specification;

public class NewsCategorySpecification {


    public static Specification<NewsCategory> byNewsCategory(String newsCategory) {
        return (root, query, criteriaBuilder) -> {
            if (newsCategory == null || newsCategory.isEmpty() || newsCategory.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("category"), newsCategory);

        };
    }
}
