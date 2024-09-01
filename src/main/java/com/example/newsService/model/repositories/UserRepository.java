package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "news")
    Optional<User> findById(Long userId);
//    @EntityGraph(attributePaths = "newsList")
    Page<User> findAll(Pageable pageable);



}
