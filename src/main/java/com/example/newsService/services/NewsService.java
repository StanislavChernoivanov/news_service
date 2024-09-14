package com.example.newsService.services;

import com.example.newsService.model.entities.News;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;

import java.util.List;

public interface NewsService {

    List<News> findAll(RequestPageableModel requestPageableModel);

    News findById(Long newsId);

    News save(News news, Long userId, Long newsCategoryId);

    News update(Long newsId,Long userId, News news);

    void delete(Long newsId);

    List<News> filterBy(
            Long userId,
            Long newsCategoryId,
            RequestPageableModel model);

    void checkAccessByUser(Long userId, Long newsId);

}
