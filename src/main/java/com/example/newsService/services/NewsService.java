package com.example.newsService.services;

import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;

import java.util.List;

public interface NewsService {

    List<News> findAll(RequestPageableModel requestPageableModel);

    GenericModel<Integer, News> findById(Long newsId);

    News save(News news);

    News update(Long newsId, News news);

    void delete(Long newsId);

    List<News> filterBy(NewsFilter newsFilter);

    void checkAccessByUser(Long userId, Long newsId);

}
