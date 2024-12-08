package com.example.newsService.services.impl;

import com.example.newsService.configuration.cache.AppCacheProperties;
import com.example.newsService.exceptions.EntityIsNotUniqueException;
import com.example.newsService.exceptions.EntityIsNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.model.repositories.specifications.NewsCategorySpecification;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository repository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.NEWS_CATEGORIES,
        key = "#model.getPageSize() + #model.getPageNumber()")
    public List<NewsCategory> findAll(RequestPageableModel model) {
        Page<NewsCategory> newsCategoryPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );
        System.err.println(MessageFormat.format("Класс - {0}: не закэшировано",
                getClass().getSimpleName()));

        return newsCategoryPage.stream().peek(c -> c.setNewsList(withoutComments(c))).toList();
    }

    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.NEWS_CATEGORY_BY_ID)
    public NewsCategory findById(Long newsCategoryId) {
        NewsCategory category = repository.findById(newsCategoryId).orElseThrow(() ->
                new EntityIsNotFoundException(
                        String.format("Категория новостей с id %s не найдена", newsCategoryId)
                ));
        category.setNewsList(withoutComments(category));
        System.err.println(MessageFormat.format("Класс - {0}: не закэшировано",
                getClass().getSimpleName()));
        return category;
    }

    @Override
    @CacheEvict(
            cacheNames = AppCacheProperties.CacheNames.NEWS_CATEGORIES,
            allEntries = true
    )
    public NewsCategory save(NewsCategory newsCategory) {

        List<NewsCategory> newsCategoryFromDB = repository.findAll(
                NewsCategorySpecification.byNewsCategory(newsCategory.getCategory())
        );

        if (!newsCategoryFromDB.isEmpty()) throw new EntityIsNotUniqueException(
                MessageFormat.format("Категория новостей - {0} уже существует"
                        , newsCategory.getCategory()));

        newsCategory = repository.save(newsCategory);
        return repository.save(newsCategory);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_CATEGORIES
            , allEntries = true),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.NEWS_CATEGORY_BY_ID
            , key = "#newsCategoryId")
    })
    public void delete(Long newsCategoryId) {
        repository.deleteById(newsCategoryId);
    }



    private List<News> withoutComments(NewsCategory c) {
        List<News> newsListOfEachCategory = c.getNewsList();
        List<News> newsListWithoutLazyComments = new ArrayList<>(newsListOfEachCategory.size());
        for (News news : newsListOfEachCategory) {
            int commentsAmountOfEachNews = commentRepository.countByNewsId(news.getId());
            newsListWithoutLazyComments.add(News.builder().id(news.getId())
                    .header(news.getHeader()).description(news.getDescription())
                    .newsCategory(news.getNewsCategory())
                    .commentsAmount(commentsAmountOfEachNews)
                    .category(news.getNewsCategory().getCategory())
                    .build());
            c.setNewsList(newsListWithoutLazyComments);
        }
        return newsListWithoutLazyComments;
    }


}
