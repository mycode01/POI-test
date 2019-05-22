package com.example.docsdemo.word.service;

import lombok.Getter;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class WordReadService implements WordService{
    @Getter
    private final String TARGET;

    public WordReadService(String target) {
        this.TARGET = target;
    }

    public String extractParagraph() {
        String str = "";
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
            java.util.List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
//                System.out.println(paragraph.getText());
                str += paragraph.getText();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }

    public String extractText() {
        String str = "";
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            XWPFDocument file = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor ext = new XWPFWordExtractor(file);
//            System.out.println(ext.getText());
            str = ext.getText();
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }

    public List<HashMap<String, String>> getTables() {
        List<HashMap<String, String>> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
            boolean isTableNext = false; // 의미가 이상하지만 관리해야할 테이블인지 구분
            for (IBodyElement elem : doc.getBodyElements()) {
                if (elem instanceof XWPFParagraph) {
                    String ts = ((XWPFParagraph) elem).getText();
                    if (Pattern.compile("테이블[\\d]\\:").matcher(ts).matches()) {
                        isTableNext = true;
                    }
                    System.out.println(ts);
                } else if (isTableNext && elem instanceof XWPFTable) {
                    HashMap<String, String> map = new HashMap<>();
                    String ts = ((XWPFTable) elem).getText();
                    String[] ta = ts.split("\t|\n");
                    for (int i = 1; i < ta.length; i += 2) {
                        map.put(ta[i - 1], ta[i]);
                    }
                    System.out.println(map);
                    list.add(map);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public String writeLine() throws Exception {
        XWPFDocument document = new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(new File("helloworld.docx"))) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("howdy ho!");

            document.write(out);
            System.out.println("createdocument.docx written successully");
        } catch (Exception e) {

        } finally {
        }
        return "";
    }
}
