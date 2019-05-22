package com.example.docsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.docsdemo"})
public class DocsdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocsdemoApplication.class, args);
	}

}
