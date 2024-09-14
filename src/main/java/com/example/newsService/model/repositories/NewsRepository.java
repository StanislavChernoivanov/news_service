package com.example.newsService.model.repositories;

import com.example.newsService.model.entities.News;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    @Query("select count(n) from News n where id = :newsId and n.user.id = :userId")
    int countNewsByIdAndUserId(Long newsId, Long userId);


    @EntityGraph(attributePaths = "commentsList")
    Optional<News> findById(Long newsId);
}
