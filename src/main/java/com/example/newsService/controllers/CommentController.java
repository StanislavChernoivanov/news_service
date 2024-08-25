package com.example.newsService.controllers;

import com.example.newsService.mapper.CommentMapper;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.services.CommentService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.toResponse.CommentListResponse;
import com.example.newsService.web.model.toResponse.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping("/{newId}")
    public ResponseEntity<CommentListResponse> findAllByNewsId(
            @PathVariable Long newsId) {
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
    public ResponseEntity<CommentResponse> create(
            @RequestBody @Valid UpsertCommentRequest upsertCommentRequest) {
        Comment comment = commentService.save(
                commentMapper.requestToComment(upsertCommentRequest));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(commentMapper.commentToResponse(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable("id") Long commentId,
            @RequestBody @Valid UpsertCommentRequest request) {
        Comment comment = commentService.update(commentId,
                commentMapper.requestToComment(commentId, request));
        return ResponseEntity.ok(commentMapper.commentToResponse(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("{id}") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
