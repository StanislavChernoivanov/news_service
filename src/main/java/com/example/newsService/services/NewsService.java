package com.example.newsService.services;

import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.web.model.filters.NewsFilter;

import java.util.List;

public interface NewsService {

    List<News> findAll();

    News findById(Long newsId);

    News save(News news);

    News update(Long newsId, News news);

    void delete(Long newsId);

    List<News> filterBy(NewsFilter newsFilter);

}
