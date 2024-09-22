package com.example.newsService;

import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsCategoryRepository;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsServiceApplicationTests {
	@LocalServerPort
	private Integer port;

	public static PostgreSQLContainer<?> container
			= new PostgreSQLContainer<>("postgres:14");
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
	protected NewsCategoryRepository newsCategory;
	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected TestRestTemplate template;

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
		User user = User.builder().name("user").surname("user").build();
		User user1 = User.builder().name("user1").surname("user1").build();
		NewsCategory newsCategory = NewsCategory.builder().category("sport").build();
		NewsCategory newsCategory1 = NewsCategory.builder().category("notsport").build();
		News news = News.builder().header("news").description("news")
				.createAt(Instant.now()).build();
		News news1 = News.builder().header("news1").description("news1")
				.createAt(Instant.now()).build();
		Comment comment = Comment.builder().comment("comment").build();
		Comment comment1 = Comment.builder().comment("comment1").build();
		news.setUser(userService.save(user));
		news1.setUser(userService.save(user1));
		news.setNewsCategory(newsCategoryService.save(newsCategory));
		news.setNewsCategory(newsCategoryService.save(newsCategory1));
		comment.setNews(newsService.save(news, user.getId(), newsCategory.getId()));
		comment1.setNews(newsService.save(news1, user1.getId(), newsCategory1.getId()));
		commentRepository.saveAll(List.of(comment, comment1));
	}

	@AfterEach
	public void clearDatabase() {
		userRepository.deleteAll();
		newsCategory.deleteAll();
	}



}
