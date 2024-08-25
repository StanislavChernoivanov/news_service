package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.NewsSpecification;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import com.example.newsService.web.model.filters.NewsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;

    private final UserService userService;

    @Override
    public List<News> findAll() {
        return repository.findAll();
    }

    @Override
    public News findById(Long newsId) {
        return repository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Новость с id %s не найдена", newsId)));
    }

    @Override
    public News save(News news) {
        User user = userService.findById(news.getUser().getId());
        news.setUser(user);
        return repository.save(news);
    }

    @Override
    public News update(Long newsId, News news) {
        User user = userService.findById(news.getUser().getId());
        News updatedNews = new News();
        BeanUtils.copyNotNullProperties(news, updatedNews);
        updatedNews.setUser(user);
        return repository.save(updatedNews);
    }


    @Override
    public void delete(Long newsId) {
        repository.deleteById(newsId);
    }

    @Override
    public List<News> filterBy(NewsFilter newsFilter) {
        return repository.findAll(NewsSpecification.withFilter(newsFilter));
    }
}
