package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByNewsId(Long newsId);


}
