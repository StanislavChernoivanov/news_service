package com.example.newsService.services;

import com.example.newsService.model.entities.News;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;

import java.util.List;

public interface NewsService {

    List<News> findAll(RequestPageableModel requestPageableModel);

    News findById(Long newsId);

    News save(News news, String username, Long newsCategoryId);

    News update(Long newsId, News news);

    void delete(Long newsId);

    List<News> filterBy(
            String username,
            Long newsCategoryId,
            RequestPageableModel model);

    void checkAccessByUser(String username, Long newsId);

}
