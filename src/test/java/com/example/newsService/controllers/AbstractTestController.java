package com.example.newsService.controllers;

import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.entities.User;
import com.example.newsService.web.model.toResponse.commentResponse.CommentResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsList;
import com.example.newsService.web.model.toResponse.userResponse.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@Sql("classpath:init.sql")
@ActiveProfiles(profiles = "test")
public abstract class AbstractTestController {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @LocalServerPort
    protected Integer port;

    public static PostgreSQLContainer<?> container
            = new PostgreSQLContainer<>("postgres:17rc1");


    @BeforeAll
    public static void beforeAll() {
        container.start();
    }
    @AfterAll
    public static void afterAll() {
        container.stop();
    }


    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

    }



    protected Comment createComment(Long id, User user, News news){
        Comment comment = new Comment();
        comment.setId(id);
        comment.setComment("Comment " + id);

        if (user != null) {
            comment.setUser(user);
            user.getCommentsList().add(comment);
        }

        if (news != null) {
            comment.setNews(news);
            news.getCommentsList().add(comment);
        }

        return comment;
    }

    protected News createNews(Long id, User user, NewsCategory newsCategory) {
        News news = new News();
        news.setId(id);
        news.setHeader("News " + id);
        news.setDescription("News " + id);
        news.setCreateAt(Instant.now());
        if (newsCategory != null) {
            news.setNewsCategory(newsCategory);
            newsCategory.getNewsList().add(news);
        }
        if(user != null) {
            news.setUser(user);;
            user.getNewsList().add(news);
        }
        return news;
    }

    protected NewsCategory createNewsCategory(Long id) {
        return NewsCategory.builder().id(id)
                .category("NewsCategory " + id).build();
    }

    protected User createUser(Long id) {
        return User.builder().id(id).username("user " + id).password("user").build();
    }


    protected CommentResponse createCommentResponse(Long id, String comment) {
        return CommentResponse.builder().id(id).comment(comment).build();
    }

    protected NewsResponse createNewsResponse(Long id
            , String header
            , String description
            , String category
            , CommentResponse commentResponse) {

        NewsResponseWithCommentsList newsResponse = new NewsResponseWithCommentsList();
        newsResponse.setId(id);
        newsResponse.setHeader(header);
        newsResponse.setDescription(description);
        newsResponse.setCategory(category);
        if(commentResponse != null) {
            newsResponse.getCommentsList().add(commentResponse);
        }

        return newsResponse;
    }

    protected NewsCategoryResponse createNewsCategoryResponse(Long id,
                  String category,
                  NewsResponseWithCommentsAmount
                  newsResponse) {

        NewsCategoryResponse newsCategoryResponse =
                new NewsCategoryResponse();
        newsCategoryResponse.setId(id);
        newsCategoryResponse.setCategory(category);
        if(newsResponse != null) {
            newsCategoryResponse.getNewsList().add(newsResponse);
        }
        return newsCategoryResponse;
    }

    protected UserResponse createUserResponse(Long id,
                                              String username,
                                              CommentResponse commentResponse,
                                              NewsResponseWithCommentsAmount newsResponse) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setUsername(username);

        if (commentResponse != null) {
            userResponse.getCommentsList().add(commentResponse);
        }

        if(newsResponse != null) {
            userResponse.getNewsList().add(newsResponse);
        }

        return userResponse;
    }
}
