package com.example.newsService.web.model.filters;

import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter {

    @NotNull(message = "ID пользователя должно быть указано")
    @Positive(message = "ID пользователя должно быть не менее 1")
    private Long userId;

    @NotNull(message = "ID категории новостей должно быть указано")
    @Positive(message = "ID категории новостей должно быть не менее 1")
    private Long newsCategoryId;

    private RequestPageableModel requestPageableModel;

}
