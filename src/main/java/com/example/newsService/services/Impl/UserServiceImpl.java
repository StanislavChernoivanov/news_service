package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return repository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));
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
}
