package com.example.newsService.services;

import com.example.newsService.model.entities.NewsCategory;

import java.util.List;

public interface NewsCategoryService {

    List<NewsCategory> findAll();

    NewsCategory findById(Long newsCategoryId);

    NewsCategory save(NewsCategory newsCategory);

}
