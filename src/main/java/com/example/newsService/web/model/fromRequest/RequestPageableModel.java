package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RequestPageableModel {

    @NotNull(message = "Размер страницы должен быть указан")
    @Positive(message = "Размер страницы должен быть не менее 1")
    private Integer pageSize;

    @NotNull(message = "Номер страницы должен быть указан")
    @Positive(message = "Номер страницы должен быть не менее 1")
    private Integer pageNumber;
}
