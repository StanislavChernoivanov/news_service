package com.example.newsService.services.impl;

import com.example.newsService.configuration.cache.AppCacheProperties;
import com.example.newsService.exceptions.OperationIsNotAvailableException;
import com.example.newsService.exceptions.EntityIsNotFoundException;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.USERS,
            key = "#model.getPageSize() + #model.getPageNumber()")
    public List<User> findAll(RequestPageableModel model) {
        Page<User> userPage = repository.findAll(
                PageRequest.of(model.getPageNumber(), model.getPageSize())
        );
        userPage.forEach(u -> u.setCommentsList(new ArrayList<>()));
        List<User> users = userPage.stream().toList();
        users.forEach(u -> {
            u.setNewsList(getUpdatedNewsList(u.getNewsList()));
            User.roleTypesInit(u, u.getRoles());
            }
        );

        System.err.println(MessageFormat.format("Класс - {0}: не закэшировано",
                getClass().getSimpleName()));

        return users;
    }

    @Override
    @Transactional()
    @Cacheable(cacheNames = AppCacheProperties.CacheNames.USER_BY_ID)
    public User findById(Long userId) {

        User user = repository.findById(userId).orElseThrow(() ->
                new EntityIsNotFoundException(
                        String.format("Пользователь с id %s не найден", userId)
                ));
        user.setNewsList(getUpdatedNewsList(user.getNewsList()));

        System.err.println(MessageFormat.format("Класс - {0}: не закэшировано",
                getClass().getSimpleName()));

        return User.roleTypesInit(user, user.getRoles());
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.USERS
                    , allEntries = true),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.USER_BY_ID
                , key = "userId")
    })
    public User update(Long userId, User user) {
        User updatedUser = findById(userId);
        BeanUtils.copyNotNullProperties(user, updatedUser);
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);

        return User.roleTypesInit(user, user.getRoles());
    }


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.USERS
                    , allEntries = true),
            @CacheEvict(cacheNames = AppCacheProperties.CacheNames.USER_BY_ID
                    , key = "userId")
    })
    public void delete(Long userId) {
        repository.deleteById(userId);
    }



    @Override
    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.USERS
            , allEntries = true)
    public User createNewAccount(User user, RoleType roleType) {
        Role role = Role.toRole(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
        repository.save(user);

        return User.roleTypesInit(user, user.getRoles());
    }




    @Override
    @Transactional
    public User findByUsername(String username) {

        return repository.findByUsername(username).orElseThrow(
                () -> new EntityIsNotFoundException(
                        String.format("Пользователь с именем %s не найден", username))
        );
    }


    private List<News> getUpdatedNewsList(List<News> newsList) {
        newsList.forEach(n -> {
            n.setCommentsAmount(commentRepository.countByNewsId(n.getId()));
            n.setCategory(n.getNewsCategory().getCategory());
        });
        return newsList;
    }


    @Override
    @Transactional
    public void checkAccessByUser(String username, Long userId) {

        User requestedUser = findByUsername(username);
        User wantedUser = findById(userId);

        if(wantedUser.getRoles().stream().anyMatch(r -> r.getAuthority().equals(RoleType.ROLE_ADMIN))
        && !Objects.equals(wantedUser.getId(), requestedUser.getId())) {
            throw new OperationIsNotAvailableException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));
        }

        if (requestedUser.getRoles().stream().allMatch(r -> r.getAuthority().equals(RoleType.ROLE_USER))
                && !requestedUser.getId().equals(wantedUser.getId())) {
            throw new OperationIsNotAvailableException(
                    String.format(
                            "У пользователя с именем %s отсутствует доступ к данному ресурсу", username));

        }
    }
}
