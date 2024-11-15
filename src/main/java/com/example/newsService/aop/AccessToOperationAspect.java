package com.example.newsService.aop;

import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
//@Profile({"test", "prod"})
public class AccessToOperationAspect {

    private final NewsService newsService;

    private final CommentService commentService;

    private final UserService userService;


    @Pointcut("execution(public * com.example.newsService.controllers.UserController.*(..))")
    public void callAtUserController() {
    }

    @Pointcut("execution(public * com.example.newsService.controllers.NewsController.*(..))")
    public void callAtNewsController() {
    }

    @Pointcut("execution(public * com.example.newsService.controllers.CommentController.*(..))")
    public void callAtCommentController() {
    }


    @Before("callAtUserController() && args(userDetails, userId,..) && args(userDetails, userId)")
    public void userAccessChecking(JoinPoint joinPoint) {

        Object [] arguments = joinPoint.getArgs();

        UserDetails userDetails = null;
        Long userId = null;

        for(Object arg : arguments) {
           if (arg instanceof Long) {
               userId = (Long) arg;
           } else if (arg instanceof UserDetails) {
               userDetails = (UserDetails) arg;
           }
        }

        assert userDetails != null;
        userService.checkAccessByUser(userDetails.getUsername(), userId);

    }



    @Before("callAtNewsController() && args(userDetails, newsId,..) && args(userDetails, newsId)")
    public void newAccessChecking(JoinPoint joinPoint) {
        Object [] arguments = joinPoint.getArgs();

        UserDetails userDetails = null;
        Long newsId = null;

        for(Object arg : arguments) {
            if (arg instanceof Long) {
                newsId = (Long) arg;
            } else if (arg instanceof UserDetails) {
                userDetails = (UserDetails) arg;
            }
        }

        assert userDetails != null;
        newsService.checkAccessByUser(userDetails.getUsername(), newsId);
    }



    @Before("callAtCommentController() && args(userDetails, commentId,..) && args(userDetails, commentId)")
    public void commentAccessChecking(JoinPoint joinPoint) {
        Object [] arguments = joinPoint.getArgs();

        UserDetails userDetails = null;
        Long commentId = null;

        for(Object arg : arguments) {
            if (arg instanceof Long) {
                commentId = (Long) arg;
            } else if (arg instanceof UserDetails) {
                userDetails = (UserDetails) arg;
            }
        }

        assert userDetails != null;
        commentService.checkAccessByUser(userDetails.getUsername(), commentId);
    }
}



