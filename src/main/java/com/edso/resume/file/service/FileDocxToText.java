package com.edso.resume.file.service;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

@Service
public class FileDocxToText implements FileService {
    @Override
    public String convertToText(File docxFile) {
        try {
            FileInputStream fis = new FileInputStream(docxFile);
            XWPFDocument file = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(file);
            return extractor.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
