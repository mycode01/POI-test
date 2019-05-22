package com.example.docsdemo.word.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordReadServiceTest {
    @Autowired
    WordService wordService;

    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration{
        @Value("${test.word.doc}")
        String filename;

        @Bean
        public WordService wordService(){
            System.out.println(filename);
            return new DocReadService(filename);
//            return new WordReadService(filename);
        }
    }

    @Test
    public void stage02_extractParagraph(){
        String para = wordService.extractParagraph();
        assertThat(para).isNotNull().isNotEmpty();
        System.out.println(para);
    }
    @Test
    public void stage01_extractText(){
        String text = wordService.extractText();
        assertThat(text).isNotNull().isNotEmpty();
        System.out.println(text);
    }
    @Test
    public void stage03_getTables(){
        Object obj = wordService.getTables();
        assertThat(obj).isNotNull();
        assertThat((List)obj).hasSize(3);
        assertThat(((List<HashMap<String,String>>) obj).get(2)).containsKeys("속성3-1");
        assertThat(((List<HashMap<String,String>>) obj).get(1)).containsValues("Cell113");
    }
}
