package com.example.newsService.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class LoggingAspect {

    @Pointcut("execution(public * com.example.newsService.controllers.*.*(..))")
    public void controllerLog() {
    }


    @Pointcut("execution(public * com.example.newsService.services.impl.*.*(..))")
    public void serviceLog() {
    }

    @Pointcut("controllerLog() || serviceLog()")
    public void serviceControllerLog(){}

    @Before("controllerLog()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }

        if (request != null) {
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP METHOD: {}, CONTROLLER METHOD: {}.{}",
                    request.getRemoteAddr(),
                    request.getRequestURL(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringType(),
                    joinPoint.getSignature().getName());
        }
    }


    @Before("serviceLog()")
    public void doBeforeService(JoinPoint joinPoint) {
        Object [] args = joinPoint.getArgs();
        String argsStr = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NOT ANY ARGS";
        log.info("RUN SERVICE: METHOD: {}.{}, ARGS [{}]",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                argsStr);
    }


    @After("serviceControllerLog()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("Method executed successfully: {}.{}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName());
    }

    @AfterReturning(returning = "returningObject", pointcut = "serviceControllerLog()")
    public void doAfterReturning(Object returningObject, JoinPoint joinPoint) {
        log.info("Method executed successfully: {}.{}, returning value: {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                returningObject);
    }


    @Around("serviceControllerLog()")
    public Object doAfterReturning(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceeded = proceedingJoinPoint.proceed();
        long executingDuration = System.currentTimeMillis() - start;
        log.info("Method: {}.{}, duration: {} ms",
                proceedingJoinPoint.getSignature().getDeclaringType(),
                proceedingJoinPoint.getSignature().getName(),
                executingDuration);

        return proceeded;
    }

    @AfterThrowing(throwing = "ex", pointcut = "serviceControllerLog()")
    public void doAfterReturning(JoinPoint joinPoint, Exception ex) {
        log.error("Method: {}.{} throws exception: {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
