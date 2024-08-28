package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    private final UserService userService;

    private final NewsService newsService;

    @Override
    public List<Comment> findAllByNewsId(Long newsId) {
        return repository.findAllByNewsId(newsId);
    }

    @Override
    public Comment findById(Long commentId) {
        return repository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Комментарий с id %s не найден", commentId)));
    }

    @Override
    public Comment save(Comment comment) {
        User user = userService.findById(comment.getUser().getId());
        News news = newsService.findById(comment.getNews().getId()).getB();
        comment.setNews(news);
        comment.setUser(user);
        return repository.save(comment);
    }

    @Override
    public Comment update(Long commentId, Comment comment) {
        User user = userService.findById(comment.getUser().getId());
        News news = newsService.findById(comment.getNews().getId()).getB();
        Comment updatedComment = new Comment();
        BeanUtils.copyNotNullProperties(comment, updatedComment);
        updatedComment.setNews(news);
        updatedComment.setUser(user);
        return repository.save(updatedComment);
    }
    @Override
    public void checkAccessByUser(Long userId, Long commentId) {
        int countCommentsByUser = repository.countCommentsByIdAndUserId(commentId, userId);
        if (countCommentsByUser < 1) {
            throw new DeniedAccessToOperationException(
                    String
                    .format("У пользователя с id %s отсутствует доступ для редактирования" +
                    "или удаления данного коментария", userId));
        }
    }




    @Override
    public void delete(Long commentId) {
        repository.deleteById(commentId);
    }
}
