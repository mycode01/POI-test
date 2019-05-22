package com.example.docsdemo.word.service;

import lombok.Getter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class DocReadService implements WordService {
    @Getter
    private final String TARGET;

    public DocReadService(String target) {
        this.TARGET = target;
    }

    public String extractParagraph() {
        String str = "";
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (String para : paragraphs) {
                str += para;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }

    public String extractText() {
        String str = "";
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            HWPFDocument doc = new HWPFDocument(new POIFSFileSystem(fis));

            str = doc.getText().toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }

    public List<HashMap<String, String>> getTables() {
        List<HashMap<String, String>> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {

            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);

//            list = doProcedural(we);

//            테이블을 html코드처럼 구조화시킬땐 아래처럼
//            list = doParse(doc);
            list = doProcedural2(doc);
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    private List<HashMap<String, String>> doProcedural(WordExtractor we) {
        List<HashMap<String, String>> list = new ArrayList<>();

        boolean isTableNext = false;
        List<String> tableEntry = new ArrayList<>();
        for (String elem : we.getParagraphText()) {
            elem = elem.replaceAll("\u0007", "");
            if (Pattern.compile("테이블[\\d]\\:").matcher(elem.replaceAll("\\r\\n", "")).matches()) {
                isTableNext = true;
            } else if (isTableNext && Pattern.compile("\\r\\n").matcher(elem).matches()) {
                isTableNext = false;
                HashMap<String, String> map = new HashMap<>();
                for (int i = 1; i < tableEntry.size(); i += 2) {
                    map.put(tableEntry.get(i - 1), tableEntry.get(i));
                }
                list.add(map);
                tableEntry = new ArrayList<>();
                System.out.println(map);
            } else if (isTableNext) {
                if (elem.isEmpty()) {
                    continue;
                }
                tableEntry.add(elem);
                continue;
            }
            System.out.println(elem);
        }
        return list;
    }

    private List doProcedural2(HWPFDocument doc) {
        Range range = doc.getRange();
        List<HashMap> list = new ArrayList<>();
        for (int i = 0; i < range.numParagraphs(); i++) {
            String elem = range.getParagraph(i).text();
            elem = elem.replaceAll("\u0007", "");
            System.out.println(elem);
            if (Pattern.compile("테이블[\\d]\\:").matcher(elem.replaceAll("\\r", "")).matches()) {
                Table current = range.getTable(range.getParagraph(i + 1));
                HashMap map = tableMapper(current);
                list.add(map);
                i += current.numParagraphs() + 1;
                System.out.println(map);
            }
        }

        return list;
    }

    private HashMap tableMapper(Table table) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < table.numRows(); i++) {
            TableRow tr = table.getRow(i);
            for (int j = 1; j < tr.numCells(); j += 2) {
                map.put(tr.getCell(j - 1).text(), tr.getCell(j).text());
            }
        }
        return map;
    }

    private List doParse(HWPFDocument doc) {
        Range range = doc.getRange();
        TableIterator iter = new TableIterator(range);
        List<String> tmp = new ArrayList<>();
        while (iter.hasNext()) { // expected table structure is 6x2 size
            Table table = iter.next(); // one of tables
            for (int i = 0; i < table.numRows(); i++) { // Table row like html <table> tag
                TableRow tr = table.getRow(i);
                for (int j = 0; j < tr.numCells(); j++) {
                    TableCell td = tr.getCell(j);
                    for (int k = 0; k < td.numParagraphs(); k++) {
                        Paragraph para = td.getParagraph(k);
                        tmp.add(para.text());
                    }

                }
            }
        }
        System.out.println(tmp); // 원하는 테이블만 찝어서 뽑아낼수가 없음.

        return null;
    }

}