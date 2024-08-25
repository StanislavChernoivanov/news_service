package com.example.newsService.controllers;

import com.example.newsService.mapper.NewsCategoryMapper;
import com.example.newsService.mapper.NewsMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.toResponse.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService service;

    private final NewsMapper mapper;


    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@RequestParam NewsFilter newsFilter) {
        List<News> newsList = service.filterBy(newsFilter);
        return ResponseEntity.ok(
                mapper.newsListToNewsResponseList(newsList)
        );
    }
    @GetMapping
    public ResponseEntity<NewsListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.newsListToNewsResponseList(
                        service.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long newsId) {
        return ResponseEntity.ok(
                mapper.newsToResponse(service.findById(newsId))
        );
    }


    @PostMapping
    public ResponseEntity<NewsResponse> create(
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news= service.save(
                mapper.requestToNews(upsertNewsRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsToResponse(news));
    }


    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(
            @PathVariable("id") Long newsId,
            @RequestBody @Valid UpsertNewsRequest request) {
        News news = service.update(newsId,
                mapper.requestToNews(newsId, request));
        return ResponseEntity.ok(mapper.newsToResponse(news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("{id}") Long newsId) {
        service.delete(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
