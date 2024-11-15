package com.example.newsService.services.Impl;

import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.Role;
import com.example.newsService.model.entities.RoleType;
import com.example.newsService.model.entities.User;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.UserService;
import com.example.newsService.utils.BeanUtils;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public List<User> findAll(RequestPageableModel model) {
        Page<User> userPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );
        userPage.forEach(u -> u.setCommentsList(new ArrayList<>()));
        List<User> users = userPage.stream().toList();
        users.forEach(u -> u.setNewsList(getUpdatedNewsList(u.getNewsList()))
        );
        return users;
    }

    @Override
    public User findById(Long userId) {

        User user = repository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));
        user.setNewsList(getUpdatedNewsList(user.getNewsList()));
        return user;
    }

//    @Override
//    public User save(User user) {
//        return repository.save(user);
//    }
//


    @Override
    public User update(Long userId, User user) {
        User updatedUser = new User();
        BeanUtils.copyNotNullProperties(user, updatedUser);
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(updatedUser);
    }


    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }




    @Override
    public User createNewAccount(User user, RoleType roleType) {
        user.setRoles(Collections.singletonList(Role.toRole(roleType)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }




    @Override
    public User findByUsername(String username) {
        User user =  repository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Пользователь с именем %s не найден", username))
        );
        user.setNewsList(getUpdatedNewsList(user.getNewsList()));

        return user;
    }


    private List<News> getUpdatedNewsList(List<News> newsList) {
        newsList.forEach(n -> {
            n.setCommentsAmount(commentRepository.countByNewsId(n.getId()));
            n.setCategory(n.getNewsCategory().getCategory());
        });
        return newsList;
    }


    @Override
    public void checkAccessByUser(String username, Long userId) {

        User requestedUser = findByUsername(username);
        User wantedUser = findById(userId);

        if (!requestedUser.getId().equals(wantedUser.getId())) {
            throw new DeniedAccessToOperationException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));
        }
    }
}
