package com.edso.resume.file.service;

import lombok.SneakyThrows;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FilePdfToText implements FileService {

    @SneakyThrows
    @Override
    public String convertToText(File pdfFile) {
        PDFParser pdfParser = new PDFParser(new RandomAccessFile(pdfFile, "r"));
        pdfParser.parse();

        COSDocument cosDocument = pdfParser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = new PDDocument(cosDocument);
        String textParsed = pdfTextStripper.getText(pdDocument);
        cosDocument.close();

        return textParsed;
    }
}
