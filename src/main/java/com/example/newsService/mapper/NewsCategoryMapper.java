package com.example.newsService.mapper;

import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryListResponse;
import com.example.newsService.web.model.toResponse.newsCategoryResponse.NewsCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface NewsCategoryMapper {

    NewsCategory requestToNewsCategory(UpsertNewsCategoryRequest upsertNewsCategoryRequest);

    @Mapping(source = "newsCategoryId", target = "id")
    NewsCategory requestToNewsCategory(Long newsCategoryId, UpsertNewsCategoryRequest upsertNewsCategoryRequest);

    NewsCategoryResponse newsCategoryToResponse(NewsCategory newsCategory);

    List<NewsCategoryResponse> newsCategoryListToResponseList(List<NewsCategory> newsCategories);

    default NewsCategoryListResponse newsCategoryListToNewsCategoryResponseList
            (List<NewsCategory> newsCategories) {
        NewsCategoryListResponse newsCategoryListResponse =
                new NewsCategoryListResponse();
        newsCategoryListResponse.setNewsCategories(
                newsCategoryListToResponseList(newsCategories)
        );
        return newsCategoryListResponse;
    }
}
