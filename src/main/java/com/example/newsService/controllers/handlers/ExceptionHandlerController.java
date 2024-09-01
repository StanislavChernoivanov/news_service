package com.example.newsService.controllers.handlers;

import com.example.newsService.exceptions.DeniedAccessToOperationException;
import com.example.newsService.exceptions.EntityNotFoundException;
import com.example.newsService.web.model.toResponse.ErrorResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Data
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<com.example.newsService.web.model.toResponse.ErrorResponse> notFound(EntityNotFoundException ex) {
        log.error("Ошибка при попытке получить сущность", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
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

    @ExceptionHandler(DeniedAccessToOperationException.class)
    public ResponseEntity<ErrorResponse> notAccess(DeniedAccessToOperationException ex) {
        log.error("Отсутствует доступ для редактирования или удаления новости", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ErrorResponse(ex.getLocalizedMessage()));
    }

//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> notValid(SQLIntegrityConstraintViolationException ex) {
//        ex.
//        BindingResult bindingResult = ex.getBindingResult();
//        List<String> errorMessages = bindingResult.getAllErrors().
//                stream().
//                map(DefaultMessageSourceResolvable::getDefaultMessage).
//                toList();
//        String errorMessage = String.join("; ", errorMessages);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
//                body(new ErrorResponse(errorMessage));
//    }
}
