package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.AttemptAddingNotUniqueElementException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.model.repositories.specifications.NewsCategorySpecification;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    public List<NewsCategory> findAll(RequestPageableModel model) {
        Page<NewsCategory> newsCategoryPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );

        newsCategoryPage.stream()
                .toList()
                .forEach(c -> c.setNewsList(withoutComments(c)));

        return newsCategoryPage.stream().toList();
    }

    @Override
    public NewsCategory findById(Long newsCategoryId) {
        NewsCategory category = repository.findById(newsCategoryId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Категория новостей с id %s не найдена", newsCategoryId)
                ));
        category.setNewsList(withoutComments(category));
        return category;
    }

    @Override
    public NewsCategory save(NewsCategory newsCategory) {

        List<NewsCategory> newsCategoryFromDB = repository.findAll(
                NewsCategorySpecification.byNewsCategory(newsCategory.getCategory())
        );

        if(!newsCategoryFromDB.isEmpty()) throw new AttemptAddingNotUniqueElementException(
            MessageFormat.format("Категория новостей - {0} уже существует"
                    , newsCategory.getCategory()));

        newsCategory = repository.save(newsCategory);
        return repository.save(newsCategory);
    }

    @Override
    public void delete(Long newsCategoryId) {
        repository.deleteById(newsCategoryId);
    }

    private List<News> withoutComments(NewsCategory c) {
        List<News> newsListOfEachCategory = c.getNewsList();
        List<News> newsListWithoutLazyComments = new ArrayList<>(newsListOfEachCategory.size());
        for(News news : newsListOfEachCategory) {
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
