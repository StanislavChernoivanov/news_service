package com.example.newsService.aop;

import com.example.newsService.controllers.NewsController;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
//@Profile({"test", "prod"})
public class AccessToOperationAspect {

    private final NewsService newsService;

    private final CommentService commentService;


    @Before("@annotation(Accessible)")
    public void accessChecking(JoinPoint joinPoint) {
        String className = joinPoint
                .getSourceLocation()
                .getWithinType()
                .getSimpleName();
        Object[] arguments = joinPoint.getArgs();
        Long newsOrCommentId = (Long) arguments[0];
        Long userId = (Long) arguments[1];
        if (className.equals(NewsController.class.getSimpleName())) {
            newsService.checkAccessByUser(userId, newsOrCommentId);
        } else {
            commentService.checkAccessByUser(userId, newsOrCommentId);
        }
    }
}



