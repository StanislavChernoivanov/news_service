package com.example.newsService.mapper;

import com.example.newsService.model.entities.Comment;
import com.example.newsService.model.entities.News;
import com.example.newsService.model.entities.NewsCategory;
import com.example.newsService.web.model.fromRequest.UpsertNewsCategoryRequest;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.toResponse.NewsCategoryListResponse;
import com.example.newsService.web.model.toResponse.NewsCategoryResponse;
import com.example.newsService.web.model.toResponse.NewsListResponse;
import com.example.newsService.web.model.toResponse.NewsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    News requestToNews(UpsertNewsRequest upsertNewsRequest);
    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, UpsertNewsRequest upsertNewsRequest);

    NewsResponse newsToResponse(News news);

    List<NewsResponse> newsListToResponseList(List<News> news);

    default NewsListResponse newsListToNewsResponseList
            (List<News> news){
        NewsListResponse newsListResponse = new NewsListResponse();
        newsListResponse.setNewsList(newsListToResponseList(news));
        return newsListResponse;
    }
}
