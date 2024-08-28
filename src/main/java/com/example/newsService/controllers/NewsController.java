package com.example.newsService.controllers;

import com.example.newsService.aop.Accessible;
import com.example.newsService.mapper.NewsMapper;
import com.example.newsService.model.entities.News;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.GenericModel;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.filters.NewsFilter;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
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

    private final NewsService newsService;

    private final CommentService commentService;

    private final NewsMapper mapper;


    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@RequestParam NewsFilter newsFilter) {
        List<News> newsList = newsService.filterBy(newsFilter);
        return ResponseEntity.ok(
                mapper.newsListToNewsResponseList(newsList)
        );
    }
    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(RequestPageableModel requestPageableModel) {
        return ResponseEntity.ok(
                mapper.newsListToNewsResponseList(
                        newsService.findAll(requestPageableModel)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long newsId) {
        GenericModel<Integer, News> model = newsService.findById(newsId);

        NewsResponse newsResponse = mapper.newsToResponse(model.getB());
        newsResponse.setCommentsAmount(model.getA());
        return ResponseEntity.ok(newsResponse);
    }


    @PostMapping
    public ResponseEntity<NewsResponse> create(
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news= newsService.save(
                mapper.requestToNews(upsertNewsRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsToResponse(news));
    }


    @PutMapping("/{id}")
    @Accessible
    public ResponseEntity<NewsResponse> update(
            @PathVariable("id") Long newsId,
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news = newsService.update(newsId,
                mapper.requestToNews(newsId, upsertNewsRequest));
        return ResponseEntity.ok(mapper.newsToResponse(news));
    }

    @DeleteMapping("/{id}")
    @Accessible
    public ResponseEntity<Void> delete(@PathVariable("{id}") Long newsId,
                                       @RequestParam Long userId) {
        newsService.delete(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
