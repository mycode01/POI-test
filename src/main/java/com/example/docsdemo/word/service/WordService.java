package com.example.docsdemo.word.service;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public interface WordService {

    public String extractParagraph();

    public String extractText();

    public List<HashMap<String, String>> getTables();
}
