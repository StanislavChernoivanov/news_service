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
        News news = newsService.findById(newsId);
        return news.getCommentsList();
    }

    @Override
    public Comment findById(Long commentId) {
        return repository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Комментарий с id %s не найден", commentId)));
    }

    @Override
    public Comment save(String username, Long newsId, Comment comment) {
        User user = userService.findByUsername(username);
        News news = newsService.findById(newsId);
        comment.setNews(news);
        comment.setUser(user);
        return repository.save(comment);
    }

    @Override
    public Comment update(Long commentId, Comment comment) {
        Comment newComment = findById(commentId);
        BeanUtils.copyNotNullProperties(comment, newComment);

        return repository.save(newComment);
    }


    @Override
    public void checkAccessByUser(String username, Long commentId) {
        User user = userService.findByUsername(username);
        Comment comment = findById(commentId);

        if(!user.getId().equals(comment.getUser().getId())) {
            throw new DeniedAccessToOperationException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));
        }
    }

    @Override
    public void delete(Long commentId) {
        repository.deleteById(commentId);
    }
}
