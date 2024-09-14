package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.specifications.NewsSpecification;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final NewsCategoryService newsCategoryService;

    @Override
    @Transactional
    public List<News> findAll(RequestPageableModel model) {

        Page<News> newsPage = newsRepository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );

        return updateNewsPage(newsPage).stream().toList();
    }

    @Override
    @Transactional
    public News findById(Long newsId) {

        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Новость с id %s не найдена", newsId)));
        news.setCategory(news.getNewsCategory().getCategory());

        return news;
    }


    @Override
    public News save(News news, Long userId, Long newsCategoryId) {
        User user = userService.findById(userId);
        NewsCategory newsCategory = newsCategoryService.findById(newsCategoryId);
        news.setUser(user);
        news.setNewsCategory(newsCategory);
        news.setCategory(news.getNewsCategory().getCategory());
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public News update(Long newsId, Long userId, News news) {
        News newNews = findById(newsId);
        BeanUtils.copyNotNullProperties(news, newNews);
        newNews.setCategory(newNews.getNewsCategory().getCategory());
        return newsRepository.save(newNews);

    }


    @Override
    public void delete(Long newsId) {
        newsRepository.deleteById(newsId);
    }

    @Override
    public List<News> filterBy(
            Long userId,
            Long newsCategoryId,
            RequestPageableModel model) {

        newsCategoryService.findById(newsCategoryId);
        userService.findById(userId);

        Page<News> newsPage = newsRepository.findAll(
                NewsSpecification.withFilter(userId, newsCategoryId),
                PageRequest.of(model.getPageNumber(),
                                model.getPageSize()));

        return updateNewsPage(newsPage).stream().toList();
    }

    @Override
    @Transactional
    public void checkAccessByUser(Long userId, Long newsId) {
        News news = findById(newsId);
        if (!news.getUser().getId().equals(userId)) {
            throw new DeniedAccessToOperationException(
                    String.format(
                    "У пользователя с id %s отсутствует доступ для редактирования " +
                    "или удаления данной новости", userId));
        }
    }


    private Page<News> updateNewsPage(Page<News> newsPage) {
        newsPage.forEach(news -> news.setCommentsAmount(
                commentRepository.countByNewsId(news.getId())));
        newsPage.forEach(n -> n.setCategory(n.getNewsCategory().getCategory()));
        return newsPage;
    }
}
