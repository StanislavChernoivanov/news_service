package com.example.newsService;

import com.example.newsService.model.entities.*;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@Sql("classpath:init.sql")
@ActiveProfiles(profiles = "test")
public class NewsServiceApplicationTests {
	@LocalServerPort
	protected Integer port;

	public static PostgreSQLContainer<?> container
			= new PostgreSQLContainer<>("postgres:17rc1");
	@Autowired
	protected CommentService commentService;

	@Autowired
	protected NewsService newsService;
	@Autowired
	protected NewsCategoryService newsCategoryService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected CommentRepository commentRepository;
	@Autowired
	protected NewsRepository newsRepository;
	@Autowired
	protected NewsCategoryRepository newsCategoryRepository;
	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;


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

	@BeforeEach
	public void fillingDatabase() {
		User user = User.builder().username("user").password("user").build();
		User user1 = User.builder().username("user1").password("user1").build();
		Role role = Role.builder().user(user).authority(RoleType.ROLE_USER).build();
		Role role1 = Role.builder().user(user1).authority(RoleType.ROLE_ADMIN).build();
		user.setRoleTypes(List.of(role.getAuthority()));
		user.setRoles(List.of(role));
		user1.setRoleTypes(List.of(role1.getAuthority()));
		user1.setRoles(List.of(role1));
		NewsCategory newsCategory = NewsCategory.builder().category("sport").build();
		NewsCategory newsCategory1 = NewsCategory.builder().category("notsport").build();
		News news = News.builder().header("news").description("news")
				.createAt(Instant.now()).build();
		News news1 = News.builder().header("news1").description("news1")
				.createAt(Instant.now()).build();
		Comment comment = Comment.builder().comment("comment").build();
		Comment comment1 = Comment.builder().comment("comment1").build();
		news.setUser(userRepository.saveAndFlush(user));
		news1.setUser(userRepository.saveAndFlush(user1));
		news.setNewsCategory(newsCategoryRepository.saveAndFlush(newsCategory));
		news1.setNewsCategory(newsCategoryRepository.saveAndFlush(newsCategory1));
		news.getCommentsList().add(comment);
		news1.getCommentsList().add(comment1);
		comment.setNews(newsRepository.saveAndFlush(news));
		comment1.setNews(newsRepository.saveAndFlush(news1));
		commentRepository.saveAllAndFlush(List.of(comment, comment1));
	}

	@AfterEach
	public void clearDatabase() {
		userRepository.deleteAll();
		newsCategoryRepository.deleteAll();
	}



}
