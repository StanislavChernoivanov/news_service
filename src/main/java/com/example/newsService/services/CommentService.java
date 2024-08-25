package com.example.newsService.services;

import com.example.newsService.model.entities.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByNewsId(Long newsId);

    Comment findById(Long commentId);

    Comment save(Comment comment);

    Comment update(Long commentId, Comment comment);

    void delete(Long commentId);
}
