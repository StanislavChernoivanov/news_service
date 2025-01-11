package com.example.newsService.services.impl;

import com.example.newsService.configuration.cache.AppCacheProperties;
import com.example.newsService.exceptions.OperationIsNotAvailableException;
import com.example.newsService.exceptions.EntityIsNotFoundException;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.RoleType;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final UserService userService;

    private final NewsService newsService;



    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.COMMENTS_BY_NEWS_ID)
    public List<Comment> findAllByNewsId(Long newsId) {
        News news = newsService.findById(newsId);

        return news.getCommentsList();
    }

    @Override
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.COMMENT_BY_ID)
    public Comment findById(Long commentId) {

        return repository.findById(commentId).orElseThrow(() ->
                new EntityIsNotFoundException(
                        String.format("Комментарий с id %s не найден", commentId)));

    }

    @Override
    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.COMMENTS_BY_NEWS_ID,
            key = "#newsId")
    public Comment save(String username, Long newsId, Comment comment) {
        User user = userService.findByUsername(username);
        News news = newsService.findById(newsId);
        comment.setNews(news);
        comment.setUser(user);
        return repository.save(comment);
    }



    @Override
    @Caching(evict = {
        @CacheEvict(cacheNames = AppCacheProperties.CacheNames.COMMENTS_BY_NEWS_ID,
                allEntries = true),
        @CacheEvict(cacheNames = AppCacheProperties.CacheNames.COMMENT_BY_ID,
                key = "#commentId")
    })
    public Comment update(Long commentId, Comment comment) {
        Comment newComment = findById(commentId);
        BeanUtils.copyNotNullProperties(comment, newComment);

        return repository.save(newComment);
    }


    @Override
    @Transactional
    public void checkAccessByUser(String username, Long commentId) {
        User user = userService.findByUsername(username);
        Comment comment = findById(commentId);

        if(user.getRoles().stream().allMatch(r -> r.getAuthority().equals(RoleType.ROLE_USER))
                && !user.getId().equals(comment.getUser().getId())) {
            throw new OperationIsNotAvailableException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));
        }
    }

    @Override
    @Caching(evict = {
        @CacheEvict(cacheNames = AppCacheProperties.CacheNames.COMMENTS_BY_NEWS_ID,
                allEntries = true),
        @CacheEvict(cacheNames = AppCacheProperties.CacheNames.COMMENT_BY_ID,
                key = "#commentId"),
    })
    public void delete(Long commentId) {
        repository.deleteById(commentId);
    }
}
