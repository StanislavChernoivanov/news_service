package com.example.newsService.aop;

import com.example.newsService.controllers.CommentController;
import com.example.newsService.controllers.NewsController;
import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.repositories.CommentRepository;
import com.example.newsService.model.repositories.NewsRepository;
import com.example.newsService.model.repositories.NewsSpecification;
import com.example.newsService.model.repositories.UserRepository;
import com.example.newsService.services.CommentService;
import com.example.newsService.services.NewsService;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessToOperationAspect {

    private final NewsService newsService;

    private final CommentService commentService;



    @Before("@annotation(Accessible)")
    public void accessChecking(JoinPoint joinPoint) {
        Object [] arguments = joinPoint.getArgs();
        String fileName = joinPoint.getSourceLocation().getWithinType().getName();
        Long id = (Long) arguments[0];
        if(fileName.equals(CommentController.class.getSimpleName())) {
            try {
                Long userId = (Long) arguments[1];
                commentService.checkAccessByUser(userId, id);

            } catch (ClassCastException ex) {
                UpsertCommentRequest upsertCommentRequest = (UpsertCommentRequest) arguments[1];
                Long userId = upsertCommentRequest.getUserId();
                commentService.checkAccessByUser(userId, id);
            }
        } else {
            try {
                Long userId = (Long) arguments[1];
                newsService.checkAccessByUser(userId, id);

            } catch (ClassCastException ex) {
                UpsertNewsRequest upsertNewsRequest = (UpsertNewsRequest) arguments[1];
                Long userId = upsertNewsRequest.getUserId();
                newsService.checkAccessByUser(userId, id);
            }
        }



    }


}
