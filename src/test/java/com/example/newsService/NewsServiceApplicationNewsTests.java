package com.example.newsService;

import com.example.newsService.model.entities.News;
import com.example.newsService.web.model.fromRequest.RequestPageableModel;
import com.example.newsService.web.model.fromRequest.UpsertNewsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.JsonAssert;
import org.hibernate.type.format.jackson.JacksonXmlFormatMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewsServiceApplicationNewsTests extends NewsServiceApplicationTests {
    @Test
    @DisplayName("Test get all news")
    @WithMockUser(username = "user")
    public void whenFindAll_thenReturnAllNews() throws Exception {

        RequestPageableModel model = new RequestPageableModel(5, 0);
        String actualResponse = mockMvc.perform(get("http://localhost:" + port + "/api/news").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String expectedResponse = StringTestUtil
                .readStringFromResource("responses/newsResponses/findAll/find_all_news_response.json");

        RequestPageableModel model1 = new RequestPageableModel(1, 0);
        String actualResponse1 = mockMvc.perform(get(
                        "http://localhost:" + port + "/api/news").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model1)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse1 = StringTestUtil
                .readStringFromResource("responses/newsResponses/findAll/find_all_with_pageable_news_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
        JsonAssert.assertJsonEquals(expectedResponse1, actualResponse1);

    }

    @Test
    @DisplayName("Find news by id")
    @WithMockUser(username = "user")
    public void whenFindById_thenReturnNewsById() throws Exception {
        String actualResponse = mockMvc.perform(get("http://localhost:"
                        + port + "/api/news/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil.readStringFromResource(
                "responses/newsResponses/findById/find_by_id_news_response.json"
        );

        String actual1Response = mockMvc.perform(get("http://localhost:"
                        + port + "/api/news/3"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expected1Response = StringTestUtil.readStringFromResource(
                "responses/newsResponses/findById/find_by_id_news_error_response.json"
        );

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
        JsonAssert.assertJsonEquals(expected1Response, actual1Response);
    }


    @Test
    @DisplayName("Create news")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenCreate_thenReturnCreatedNews() throws Exception {
        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(1L);
        request.setDescription("new news!");
        request.setHeader("NEW NEWS");
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.post(
                                "http://localhost:" + port + "/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil.readStringFromResource(
                "responses/newsResponses/create/create_news_response.json"
        );

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Update news")
    @WithMockUser(username = "user")
    public void whenUpdate_thenReturnUpdatedNews() throws Exception {
        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(1L);
        request.setDescription("new news!");
        request.setHeader("NEW NEWS");
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.put(
                                "http://localhost:" + port
                                        + "/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtil.readStringFromResource(
                "responses/newsResponses/update/update_news_response.json"
        );
//
//        String actual1Response = mockMvc.perform(MockMvcRequestBuilders.put(
//                                "http://localhost:" + port
//                                        + "/api/news//1?userId=2&newsId=1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotAcceptable())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        String expected1Response = StringTestUtil.readStringFromResource(
//                "responses/newsResponses/update/update_news_when_not_acceptable_response.json"
//        );

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
//        JsonAssert.assertJsonEquals(expected1Response, actual1Response);
    }


    @Test
    @DisplayName("Delete by id")
    @WithMockUser(username = "user")
    public void whenDeleteById_thenReturnVoid() throws Exception {
        Optional<News> actualNews = newsRepository.findById(1L);
        Assertions.assertTrue(actualNews.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:" + port +
                        "/api/news/1"))
                .andExpect(status().isNoContent()).andReturn();

        actualNews = newsRepository.findById(1L);
        Assertions.assertTrue(actualNews.isEmpty());


//        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
//                        .delete("http://localhost:" + port +
//                                "/api/news//2?userId=1"))
//                .andExpect(status().isNotAcceptable())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);

//        String expectedResponse = StringTestUtil.readStringFromResource(
//                "responses/newsResponses/deleteById/" +
//                        "delete_by_id_news_when_not_acceptable_response.json"
//        );

//        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

}
