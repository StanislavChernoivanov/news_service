package com.example.newsService.mapper;

import com.example.newsService.model.entities.News;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.example.newsService.web.model.toResponse.newsResponse.NewsListResponse;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsAmount;
import com.example.newsService.web.model.toResponse.newsResponse.NewsResponseWithCommentsList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    News requestToNews(UpsertNewsRequest upsertNewsRequest);

    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, UpsertNewsRequest upsertNewsRequest);

    NewsResponseWithCommentsList newsToResponse(News news);

    List<NewsResponseWithCommentsAmount> newsListToResponseList(List<News> news);

    default NewsListResponse newsListToNewsResponseList
            (List<News> news) {

        return new NewsListResponse(newsListToResponseList(news));
    }
}
