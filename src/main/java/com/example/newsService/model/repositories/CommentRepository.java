package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("select count(c) from Comment c where id = :commentId and c.user.id = :userId")
    int countCommentsByIdAndUserId(Long commentId, Long userId);

    @Query("select count(c) from Comment c where c.news.id = :newsId")
    Integer countByNewsId(Long newsId);
}
