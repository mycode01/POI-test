package com.example.docsdemo.excel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelConfig {
    @Value("${test.excel}")
    private String target;
    @Bean
    public String target(){
        return target;
    }
    @Bean
    public ExcelReadService excelReadService(){
        return new ExcelReadService(target);
    }
}
