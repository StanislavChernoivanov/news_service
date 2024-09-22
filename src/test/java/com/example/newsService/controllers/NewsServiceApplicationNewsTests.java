package com.example.newsService.controllers;

import com.example.newsService.AbstractTestController;
import com.example.newsService.NewsServiceApplicationTests;
import com.example.newsService.StringTestUtil;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewsServiceApplicationNewsTests extends NewsServiceApplicationTests {
    @Test
    @DisplayName("Test get all news")
    public void whenFindAll_thenReturnAllNews() throws Exception {
        RequestPageableModel model = new RequestPageableModel(5, 0);
        String actualResponse = mockMvc.perform(get("/api/news/").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/newsResponses/find_all_news_response.json");

        RequestPageableModel model1 = new RequestPageableModel(1, 0);
        String actualResponse1 = mockMvc.perform(get("/api/news/").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model1)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse1 = StringTestUtil
                .readStringFromResource("responses/newsResponses/find_all_with_pageable_news_response.json");

        Mockito.verify(newsRepository, Mockito.times(1)).findAll();
        Mockito.verify(newsService, Mockito.times(1)).findAll(model);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
        JsonAssert.assertJsonEquals(expectedResponse1, actualResponse1);

    }



}
