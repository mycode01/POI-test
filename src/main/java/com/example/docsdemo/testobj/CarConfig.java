package com.example.docsdemo.testobj;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarConfig {

    @Bean
    public Engine engine() {
        return new EngineOne("v8", 5);
    }

    @Bean
    public Transmission transmission() {
        return new Transmission("sliding");
    }
}