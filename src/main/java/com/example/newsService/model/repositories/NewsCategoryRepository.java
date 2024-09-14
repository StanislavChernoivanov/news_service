package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.NewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long>, JpaSpecificationExecutor<NewsCategory> {

    @Override
    @EntityGraph(attributePaths = "newsList")
    Page<NewsCategory> findAll(Pageable pageable);


    @Override
    @EntityGraph(attributePaths =  "newsList")
    Optional<NewsCategory> findById(Long aLong);
}
