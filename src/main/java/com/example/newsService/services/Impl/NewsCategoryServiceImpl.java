package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository repository;
    @Override
    public List<NewsCategory> findAll(RequestPageableModel model) {
        Page<NewsCategory> newsCategoryPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );


        return newsCategoryPage.stream().toList();
    }

    @Override
    public NewsCategory findById(Long newsCategoryId) {
        return repository.findById(newsCategoryId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Категория новостей с id %s не найдена", newsCategoryId)
                ));
    }

    @Override
    public NewsCategory save(NewsCategory newsCategory) {

        newsCategory = repository.save(newsCategory);
        return repository.save(newsCategory);
    }

    @Override
    public void delete(Long newsCategoryId) {
        repository.deleteById(newsCategoryId);
    }

}
