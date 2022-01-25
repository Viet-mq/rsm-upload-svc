package com.edso.resume.file.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

@Service
public class FileXlsxToText implements FileService {
    @Override
    public String convertToText(File xlsxFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream file = new FileInputStream(xlsxFile);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            //System.out.print(cell.getNumericCellValue() + "t");
                            stringBuilder.append(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            //System.out.print(cell.getStringCellValue() + "t");
                            stringBuilder.append(cell.getStringCellValue());
                            break;
                    }
                }
                stringBuilder.append("\n");
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(stringBuilder);
    }
}
