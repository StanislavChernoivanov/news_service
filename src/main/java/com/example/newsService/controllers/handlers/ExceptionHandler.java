package com.example.newsService.controllers.handlers;

import com.example.newsService.exceptions.EntityIsNotUniqueException;
import com.example.newsService.exceptions.OperationIsNotAvailableException;
import com.example.newsService.exceptions.EntityIsNotFoundException;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
@Data
@Slf4j
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityIsNotFoundException.class)
    public ResponseEntity<com.example.newsService.web.model.toResponse.ErrorResponse> notFound(EntityIsNotFoundException ex) {
        log.error("Ошибка при попытке получить сущность", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResponse(ex.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors().
                stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).
                toList();
        String errorMessage = String.join("; ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ErrorResponse(errorMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OperationIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> notAccess(OperationIsNotAvailableException ex) {
        log.error("У пользователя отсутствует доступ" +
                " для редактирования или удаления этой новости", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).
                body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityIsNotUniqueException.class)
    public ResponseEntity<ErrorResponse> notUniqueEntity(EntityIsNotUniqueException ex) {
        log.error("Попытка добавить в базу данных не уникальный элемент", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ErrorResponse(ex.getLocalizedMessage()));
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> notUniqueEntity(SQLIntegrityConstraintViolationException ex) {
        log.error("Попытка добавить в базу данных не уникальный элемент", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ErrorResponse(ex.getLocalizedMessage()));
    }


}
