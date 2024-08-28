package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.NewsSpecification;
import com.example.newsService.services.GenericModel;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    @Override
    public List<News> findAll(RequestPageableModel model) {

        Page<News> newsPage = newsRepository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );

        return newsPage.stream().toList();
    }

    @Override
    public GenericModel<Integer, News> findById(Long newsId) {

        Integer commentsCount = commentRepository.countByNewsId(newsId);
        return new GenericModel<>(commentsCount, newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Новость с id %s не найдена", newsId))));
    }

    @Override
    public News save(News news) {
        User user = userService.findById(news.getUser().getId());
        news.setUser(user);
        return newsRepository.save(news);
    }

    @Override
    public News update(Long newsId, News news) {
        User user = userService.findById(news.getUser().getId());
        News updatedNews = new News();
        BeanUtils.copyNotNullProperties(news, updatedNews);
        updatedNews.setUser(user);
        return newsRepository.save(updatedNews);
    }


    @Override
    public void delete(Long newsId) {
        newsRepository.deleteById(newsId);
    }

    @Override
    public List<News> filterBy(NewsFilter newsFilter) {

        Page<News> newsPage = newsRepository.findAll(NewsSpecification.withFilter(newsFilter),
                PageRequest.of(newsFilter.getRequestPageableModel().getPageNumber(),
                                newsFilter.getRequestPageableModel().getPageSize()));
        return newsPage.stream().toList();
    }

    @Override
    public void checkAccessByUser(Long userId, Long newsId) {
        int countNewsByUser = newsRepository.countNewsByIdAndUserId(newsId, userId);
        if (countNewsByUser < 1) {
            throw new DeniedAccessToOperationException(
                    String.format(
                    "У пользователя с id %s отсутствует доступ для редактирования" +
                    "или удаления данной новости", userId));
        }
    }
}
