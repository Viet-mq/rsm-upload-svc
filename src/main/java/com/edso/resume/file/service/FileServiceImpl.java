package com.edso.resume.file.service;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class FileServiceImpl implements FileService{

    public String PdfToText(File pdfFile) throws IOException {
        PDFParser pdfParser = new PDFParser(new RandomAccessFile(pdfFile, "r"));
        pdfParser.parse();

        COSDocument cosDocument = pdfParser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = new PDDocument(cosDocument);
        String textParsed = pdfTextStripper.getText(pdDocument);
        cosDocument.close();

        return textParsed;
    }

    public String DocxToText(File docxFile) {
        try {
            FileInputStream fis = new FileInputStream(docxFile);
            XWPFDocument file = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(file);
            return extractor.getText();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String XlsxToText(File xlsxFile){
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return String.valueOf(stringBuilder);
    }
}
