package com.example.newsService.web.model.toResponse.newsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseWithCommentsAmount extends NewsResponse {

        private Integer commentsAmount;

}
