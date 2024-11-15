package com.example.newsService.controllers;

import com.example.newsService.mapper.NewsCategoryMapper;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryListResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news_category/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "NewsCategory V1", description = "NewsCategory API V1")
public class NewsCategoryController {


    private final NewsCategoryService newsCategoryService;

    private final NewsCategoryMapper mapper;




    @GetMapping
    @Operation(
            summary = "Get all news category",
            description = "Get all news category, return news categories with news lists",
            tags = {"news category"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsCategoryListResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsCategoryListResponse> findAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid RequestPageableModel model) {
        return ResponseEntity.ok(
                mapper.newsCategoryListToNewsCategoryResponseList(
                        newsCategoryService.findAll(model)
                )
        );
    }





    @GetMapping("/{id}")
    @Operation(
            summary = "Get news category by id",
            description = "Get news category by id, return news category with news list",
            tags = {"news category", "news category id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = NewsCategoryResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsCategoryResponse> findById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long newsCategoryId) {
        return ResponseEntity.ok(
                mapper.newsCategoryToResponse(newsCategoryService.findById(newsCategoryId))
        );
    }






    @PostMapping
    @Operation(
            summary = "Create news category",
            description = "Create new news category, return news category",
            tags = {"news category"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = NewsCategoryResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<NewsCategoryResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UpsertNewsCategoryRequest upsertNewsCategoryRequest) {
        NewsCategory newsCategory = newsCategoryService.save(
                mapper.requestToNewsCategory(upsertNewsCategoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(mapper.newsCategoryToResponse(newsCategory));
    }






    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete news category by id",
            description = "Delete news category by id",
            tags = {"news category", "news category id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long newsCategoryId) {
        newsCategoryService.delete(newsCategoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
