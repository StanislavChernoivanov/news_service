package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
}
