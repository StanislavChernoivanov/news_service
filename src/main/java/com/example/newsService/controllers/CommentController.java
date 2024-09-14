package com.example.newsService.controllers;

import com.example.newsService.aop.Accessible;
import com.example.newsService.mapper.CommentMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.services.CommentService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.toResponse.commentResponse.CommentListResponse;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<CommentListResponse> findAllByNewsId(
            @RequestParam Long newsId) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentResponseList(
                        commentService.findAllByNewsId(newsId)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable("id") Long commentId) {
        return ResponseEntity.ok(
          commentMapper.commentToResponse(commentService.findById(commentId))
        );
    }


    @PostMapping
    public ResponseEntity<CommentResponse> create(Long userId, Long newsId,
            @RequestBody @Valid UpsertCommentRequest upsertCommentRequest) {
        Comment comment = commentService.save(userId, newsId,
                commentMapper.requestToComment(upsertCommentRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(commentMapper.commentToResponse(comment));
    }

    @PutMapping("/{id}")
    @Accessible
    public ResponseEntity<CommentResponse> update(
            @PathVariable("id") Long commentId,
            @RequestParam Long userId,
            @RequestBody @Valid UpsertCommentRequest upsertCommentRequest) {
        Comment comment = commentService.update(
                commentId,
                commentMapper.requestToComment(commentId, upsertCommentRequest));
        return ResponseEntity.ok(commentMapper.commentToResponse(comment));
    }

    @DeleteMapping("/{id}")
    @Accessible
    public ResponseEntity<Void> delete(@PathVariable("id") Long commentId,
                                       @RequestParam Long userId) {
        commentService.delete(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
