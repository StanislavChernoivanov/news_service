package com.example.newsService.services;

import com.example.newsService.model.entities.User;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;

import java.util.List;

public interface UserService {

    List<User> findAll(RequestPageableModel requestPageableModel);

    User findById(Long userId);

    User save(User user);

    User update(Long userId, User user);

    void delete(Long userId);
}
