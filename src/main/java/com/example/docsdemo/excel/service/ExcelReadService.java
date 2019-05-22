package com.example.docsdemo.excel.service;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ExcelReadService {
    @Getter
    private final String TARGET;

    public ExcelReadService(String target) {
        this.TARGET = target;
    }

    public String extractText() {
        String str = "";
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
//                    Optional<XSSFCell> cell = Optional.ofNullable(row.getCell(j));
//                    str += cell.map(this::getCellValue).orElse(""); // prevent NPE
//                    str += cell.getStringCellValue(); // cell can be null
                    str += row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }

    public List<List<String>> getTables() {
        List list = new ArrayList<List<String>>();
        try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(TARGET))) {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<XSSFTable> tables = sheet.getTables();
            for (XSSFTable table : tables) {
                List<String> entry = new ArrayList();
                CellReference start = table.getStartCellReference();
                CellReference end = table.getEndCellReference();
                IntStream.range(start.getRow(), end.getRow() + 1).forEach(i -> {
                    XSSFRow r = sheet.getRow(i);
                    IntStream.range(start.getCol(), end.getCol() + 1).forEach(n -> {
                        entry.add(getCellValue(r.getCell(n)));
                    });
                });

                list.add(entry);
            }
        } catch (Exception e) {
            System.out.println(e);

        }
        return list;
    }

    private String getCellValue(Cell c) {
        switch (c.getCellType()) {
            case STRING:
                return c.getStringCellValue();
            case NUMERIC:
                return Double.toString(c.getNumericCellValue());
            case FORMULA:
                return c.getCachedFormulaResultType().equals(CellType.STRING) ?
                        c.getStringCellValue() : Double.toString(c.getNumericCellValue());
            case BLANK:
                return " ";
            default:
                return "typeerror";
        }
    }
}
