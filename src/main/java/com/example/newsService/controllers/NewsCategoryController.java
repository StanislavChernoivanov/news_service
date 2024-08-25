package com.example.newsService.controllers;

import com.example.newsService.mapper.NewsCategoryMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.toResponse.CommentResponse;
import com.example.newsService.web.model.toResponse.NewsCategoryListResponse;
import com.example.newsService.web.model.toResponse.NewsCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/news_category/")
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService service;

    private final NewsCategoryMapper mapper;

    @GetMapping
    public ResponseEntity<NewsCategoryListResponse> findAll() {
        return ResponseEntity.ok(
                mapper.newsCategoryListToNewsCategoryResponseList(
                        service.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> findById(@PathVariable("id") Long newsCategoryId) {
        return ResponseEntity.ok(
                mapper.newsCategoryToResponse(service.findById(newsCategoryId))
        );
    }


    @PostMapping
    public ResponseEntity<NewsCategoryResponse> create(
            @RequestBody @Valid UpsertNewsCategoryRequest upsertNewsCategoryRequest) {
        NewsCategory newsCategory = service.save(
                mapper.requestToNewsCategory(upsertNewsCategoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsCategoryToResponse(newsCategory));
    }

}
