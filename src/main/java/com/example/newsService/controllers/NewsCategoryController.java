package com.example.newsService.controllers;

import com.example.newsService.mapper.NewsCategoryMapper;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryListResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news_category/")
@Slf4j
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;

    private final NewsCategoryMapper mapper;

    @GetMapping
    public ResponseEntity<NewsCategoryListResponse> findAll(
            @RequestBody @Valid RequestPageableModel model) {
        return ResponseEntity.ok(
                mapper.newsCategoryListToNewsCategoryResponseList(
                        newsCategoryService.findAll(model)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> findById(@PathVariable("id") Long newsCategoryId) {
        return ResponseEntity.ok(
                mapper.newsCategoryToResponse(newsCategoryService.findById(newsCategoryId))
        );
    }


    @PostMapping
    public ResponseEntity<NewsCategoryResponse> create(
            @RequestBody @Valid UpsertNewsCategoryRequest upsertNewsCategoryRequest) {
        NewsCategory newsCategory = newsCategoryService.save(
                mapper.requestToNewsCategory(upsertNewsCategoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsCategoryToResponse(newsCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long newsCategoryId) {
        newsCategoryService.delete(newsCategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
