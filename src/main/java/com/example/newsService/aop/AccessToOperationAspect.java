package com.example.newsService.aop;

import com.example.newsService.controllers.CommentController;
import com.example.newsService.controllers.NewsController;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
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
        if(className.equals(NewsController.class.getSimpleName())) {
            newsService.checkAccessByUser(userId, newsOrCommentId);
        } else {
            commentService.checkAccessByUser(userId, newsOrCommentId);
        }
    }
}



