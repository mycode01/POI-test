package com.example.docsdemo.word.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WordConfig {
    @Value("${test.word.doc}")
    private String target;
    @Bean
    public String target(){
        return target;
    }
    @Bean
    public WordService wordService(){
        return new DocReadService(target);
    }
}
