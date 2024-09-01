package com.example.newsService.aop;

import com.example.newsService.controllers.CommentController;
import com.example.newsService.web.model.fromRequest.UpsertCommentRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.example.newsService.controllers.*(..))")
    public void callAtMyServicePublic() { }

    @Before("callAtMyServicePublic()")
    public void loggingBefore(JoinPoint joinPoint) {
        log.info(MessageFormat.format("Началось выполнение метода {0}", joinPoint.getSignature()));
    }

    @After("callAtMyServicePublic")
    public void loggingAfter(JoinPoint joinPoint) {
        log.info(MessageFormat.format("Закончилось выполнение метода {0}", joinPoint.toString()));
    }

}
