package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.services.NewsCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository repository;
    @Override
    public List<NewsCategory> findAll() {
        return repository.findAll();
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
        return repository.save(newsCategory);
    }

}
