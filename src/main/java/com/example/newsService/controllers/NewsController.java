package com.example.newsService.controllers;

import com.example.newsService.aop.Accessible;
import com.example.newsService.mapper.NewsMapper;
import com.example.newsService.model.entities.News;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsListResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news/")
@RequiredArgsConstructor
@Tag(name = "News V1", description = "News API V1")
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper mapper;


    @GetMapping("/filter")@Operation(
            summary = "Get all news by user id and news category id",
            description = "Get all news by user id and news category id" +
                    ", return news list with comments amount",
            tags = {"news","user id", "news category id"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsListResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
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
    @Operation(
            summary = "Get all news",
            description = "Get all news, return news list with comments amount",
            tags = {"news"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsListResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsListResponse> findAll(@RequestBody @Valid RequestPageableModel requestPageableModel) {

        List<News> newsList = newsService.findAll(requestPageableModel);
        return ResponseEntity.ok(mapper.newsListToNewsResponseList(newsList));
    }




    @GetMapping("/{id}")
    @Operation(
            summary = "Get news by id",
            description = "Get news by id, return news with comments",
            tags = {"news","news id"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")

            )
    })
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long newsId) {

        return ResponseEntity.ok(
                mapper.newsToResponse(newsService.findById(newsId))
        );
    }




    @PostMapping
    @Operation(
            summary = "Create news by user id",
            description = "Create news by user id, return news",
            tags = {"news","user id"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = NewsResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsResponse> create(@RequestParam("userId") Long userId,
                                               @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news = newsService.save(
                mapper.requestToNews(upsertNewsRequest)
                , userId
                , upsertNewsRequest.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsToResponse(news));
    }




    @PutMapping("/{id}")
    @Accessible
    @Operation(
            summary = "Update news by id",
            description = "Update news by id, using user id for access check" +
                    ", return news with comments",
            tags = {"news","news id", "user id"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")

            ),
            @ApiResponse(
                    responseCode = "406",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsResponse> update(
            @PathVariable("id") Long newsId, @RequestParam Long userId,
            @RequestBody @Valid UpsertNewsRequest upsertNewsRequest) {
        News news = newsService.update(newsId, userId,
                mapper.requestToNews(newsId, upsertNewsRequest));
        return ResponseEntity.ok(mapper.newsToResponse(news));
    }




    @DeleteMapping("/{id}")
    @Accessible
    @Operation(
            summary = "Delete news by id",
            description = "Delete news by id, using user id for access check",
            tags = {"news","news id", "user id"}
    )
    @ApiResponses( {
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json")

            ),
            @ApiResponse(
                    responseCode = "406",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Void> delete(@PathVariable("id") Long newsId,
                                       @RequestParam Long userId) {
        newsService.delete(newsId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
