package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.NewsCategoryService;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final CommentRepository commentRepository;

    private final NewsCategoryService categoryService;

    @Override
    @Transactional
    public List<User> findAll(RequestPageableModel model) {
        Page<User> userPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );
        userPage.forEach(u -> u.setCommentsList(new ArrayList<>()));
        List<User> users = userPage.stream().toList();
        users.forEach(u -> u.setNewsList(getUpdatedUser(u.getNewsList()))
        );
        return users;
    }

    @Override
    public User findById(Long userId) {

        User user = repository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));
        user.setNewsList(getUpdatedUser(user.getNewsList()));
        return user;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(Long userId, User user) {
        User updatedUser = new User();
        BeanUtils.copyNotNullProperties(user, updatedUser);
        return updatedUser;
    }


    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }


    private List<News> getUpdatedUser(List<News> newsList) {
        newsList.forEach(n -> {
            n.setCommentsAmount(commentRepository.countByNewsId(n.getId()));
            n.setCategory(n.getNewsCategory().getCategory());
        });
        return newsList;
    }
}
