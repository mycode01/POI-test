package com.example.docsdemo.excel.service;

import com.example.docsdemo.word.service.DocReadService;
import com.example.docsdemo.word.service.WordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelReadServiceTest {
    @Autowired
    ExcelReadService excelReadService;

    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration{
        @Value("${test.excel}")
        String filename;
        @Bean
        public ExcelReadService excelReadService(){
            System.out.println(filename);
            return new ExcelReadService(filename);
        }
    }

    @Test
    public void stage01_extractText(){
        String text = excelReadService.extractText();
        assertThat(text).isNotNull().isNotEmpty();
        System.out.println(text);
    }

    @Test
    public void stage02_getTables(){
        List<List<String>> list = excelReadService.getTables();
        assertThat(list).isNotNull();
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).contains("ag");
        assertThat(list.get(1)).contains("17.0");
        System.out.println(list);
    }
}
