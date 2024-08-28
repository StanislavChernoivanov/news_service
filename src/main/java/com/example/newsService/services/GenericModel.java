package com.example.newsService.services;

import lombok.Data;

@Data
public class GenericModel<A,B> {
    private final A a;

    private final B b;


}
