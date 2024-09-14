package com.example.newsService.controllers;

import com.example.newsService.aop.Accessible;
import com.example.newsService.mapper.NewsMapper;
import com.example.newsService.model.entities.News;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.toResponse.newsResponse.NewsListResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponse;
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

    private final NewsService newsService;

    private final CommentService commentService;

    private final NewsMapper mapper;


    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(
            @RequestParam Long userId,
            @RequestParam Long newsCategoryId,
            @RequestBody RequestPageableModel model) {
        List<News> newsList = newsService.filterBy(
                userId,
                newsCategoryId,
                model);
        return ResponseEntity.ok(
                mapper.newsListToNewsResponseList(newsList)
        );
    }
    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(@RequestBody @Valid RequestPageableModel requestPageableModel) {

        List<News> newsList = newsService.findAll(requestPageableModel);
        return ResponseEntity.ok(mapper.newsListToNewsResponseList(newsList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long newsId) {

        return ResponseEntity.ok(
                mapper.newsToResponse(newsService.findById(newsId))
        );
    }


    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestParam("userId") Long userId,
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news= newsService.save(
                mapper.requestToNews(upsertNewsRequest)
                , userId
                , upsertNewsRequest.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsToResponse(news));
    }


    @PutMapping("/{id}")
    @Accessible
    public ResponseEntity<NewsResponse> update(
            @PathVariable("id") Long newsId, @RequestParam Long userId,
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news = newsService.update(newsId, userId,
                mapper.requestToNews(newsId, upsertNewsRequest));
        return ResponseEntity.ok(mapper.newsToResponse(news));
    }

    @DeleteMapping("/{id}")
    @Accessible
    public ResponseEntity<Void> delete(@PathVariable("id") Long newsId,
                                       @RequestParam Long userId) {
        newsService.delete(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
