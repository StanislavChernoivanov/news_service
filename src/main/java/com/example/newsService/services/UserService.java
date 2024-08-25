package com.example.newsService.services;

import com.example.newsService.model.entities.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long userId);

    User save(User user);

    User update(Long userId, User user);

    void delete(Long userId);
}
