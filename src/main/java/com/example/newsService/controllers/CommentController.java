package com.example.newsService.controllers;

import com.example.newsService.aop.CheckAccess;
import com.example.newsService.mapper.CommentMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.services.CommentService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import com.example.newsService.web.model.toResponse.commentResponse.CommentListResponse;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Tag(name = "Comment V1", description = "Comment API V1")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    @Operation(
            summary = "Fing all comments",
            description = "Find all comments, return all comments",
            tags = {"comment"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = CommentListResponse.class)
                    )
            )
    })
    public ResponseEntity<CommentListResponse> findAllByNewsId(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long newsId) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentResponseList(
                        commentService.findAllByNewsId(newsId)
                )
        );
    }





    @GetMapping("/{id}")
    @Operation(
            summary = "Get comment by id",
            description = "Get comment by id, return comment",
            tags = {"comment", "comment id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = CommentResponse.class),
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
    public ResponseEntity<CommentResponse> findById(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PathVariable("id") Long commentId) {
        return ResponseEntity.ok(
                commentMapper.commentToResponse(commentService.findById(commentId))
        );
    }





    @PostMapping
    @Operation(
            summary = "Create comment",
            description = "Create new comment by user id and news id, return comment",
            tags = {"comment", "user id", "news id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = CommentResponse.class)
                    )
            )
    })
    public ResponseEntity<CommentResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                  Long newsId,
                                                  @RequestBody @Valid UpsertCommentRequest upsertCommentRequest) {
        Comment comment = commentService.save(userDetails.getUsername(), newsId,
                commentMapper.requestToComment(upsertCommentRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(commentMapper.commentToResponse(comment));
    }






    @PutMapping("/{id}")
    @CheckAccess
    @Operation(
            summary = "Update comment by id",
            description = "Update comment by id, using user id for access check return comment",
            tags = {"comment", "comment id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = CommentResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<CommentResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long commentId,
            @RequestBody @Valid UpsertCommentRequest upsertCommentRequest) {
        Comment comment = commentService.update(
                commentId,
                commentMapper.requestToComment(commentId, upsertCommentRequest));
        return ResponseEntity.ok(commentMapper.commentToResponse(comment));
    }







    @DeleteMapping("/{id}")
    @CheckAccess
    @Operation(
            summary = "Delete comment by id",
            description = "Delete comment by id, using user id for access check",
            tags = {"comment", "comment id"}
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
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable("id") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
