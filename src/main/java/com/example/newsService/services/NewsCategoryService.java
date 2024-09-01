package com.example.newsService.services;

import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;

import java.util.List;

public interface NewsCategoryService {

    List<NewsCategory> findAll(RequestPageableModel requestPageableModel);

    NewsCategory findById(Long newsCategoryId);

    NewsCategory save(NewsCategory newsCategory);

    void delete(Long newsCategoryId);
}
