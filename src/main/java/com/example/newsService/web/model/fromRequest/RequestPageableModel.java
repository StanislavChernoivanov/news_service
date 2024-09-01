package com.example.newsService.web.model.fromRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPageableModel {

    @NotNull(message = "Размер страницы должен быть указан")
    @Positive(message = "Размер страницы должен быть не менее 1")
    private Integer pageSize;

    @NotNull(message = "Номер страницы должен быть указан")
    @PositiveOrZero(message = "Номер страницы должен быть не менее 0")
    private Integer pageNumber;
}
