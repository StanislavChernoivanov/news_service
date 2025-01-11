package com.example.newsService.services.impl;

import com.example.newsService.configuration.cache.AppCacheProperties;
import com.example.newsService.exceptions.OperationIsNotAvailableException;
import com.example.newsService.exceptions.EntityIsNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.entities.RoleType;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
    @Cacheable(
            cacheNames = AppCacheProperties.CacheNames.NEWS,
            key = "#model.getPageSize() + #model.getPageNumber()")
    public List<News> findAll(RequestPageableModel model) {

        Page<News> newsPage = newsRepository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );

        return updateNewsPage(newsPage).stream().toList();
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_ID)
    public News findById(Long newsId) {

        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new EntityIsNotFoundException(
                        String.format("Новость с id %s не найдена", newsId)));
        news.setCategory(news.getNewsCategory().getCategory());

        return news;
    }


    @Override
    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS
    , allEntries = true)
    public News save(News news, String username, Long newsCategoryId) {
        User user = userService.findByUsername(username);
        NewsCategory newsCategory = newsCategoryService.findById(newsCategoryId);
        news.setUser(user);
        news.setNewsCategory(newsCategory);
        news.setCategory(news.getNewsCategory().getCategory());
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS
            , allEntries = true),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_ID
            , key = "#newsId"),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_USERNAME_AND_CATEGORY_ID
                    , allEntries = true)
    })
    public News update(Long newsId, News news) {
        News newNews = findById(newsId);
        BeanUtils.copyNotNullProperties(news, newNews);
        newNews.setCategory(newNews.getNewsCategory().getCategory());
        return newsRepository.save(newNews);

    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS
                    , allEntries = true),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_ID
                    , key = "#newsId"),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_USERNAME_AND_CATEGORY_ID
            , allEntries = true)
    })
    public void delete(Long newsId) {
        newsRepository.deleteById(newsId);
    }





    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.NEWS_BY_USERNAME_AND_CATEGORY_ID
        , key = "#username + #categoryId")
    public List<News> filterBy(
            String username,
            Long newsCategoryId,
            RequestPageableModel model) {

        newsCategoryService.findById(newsCategoryId);
        User user = userService.findByUsername(username);

        Page<News> newsPage = newsRepository.findAll(
                NewsSpecification.withFilter(user.getId(), newsCategoryId),
                PageRequest.of(model.getPageNumber(),
                        model.getPageSize()));

        return updateNewsPage(newsPage).stream().toList();
    }



    @Override
    @Transactional
    public void checkAccessByUser(String username, Long newsId) {
        User user = userService.findByUsername(username);
        News news = findById(newsId);

        if (user.getRoles().stream().allMatch(r -> r.getAuthority().equals(RoleType.ROLE_USER))
                && !user.getId().equals(news.getUser().getId())) {
            throw new OperationIsNotAvailableException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));
        }
    }


    private Page<News> updateNewsPage(Page<News> newsPage) {
        newsPage.forEach(news -> {
            news.setCommentsAmount(
            commentRepository.countByNewsId(news.getId()));
            String category = news.getNewsCategory().getCategory();
            news.setCategory(category);
        });

        return newsPage;
    }
}
