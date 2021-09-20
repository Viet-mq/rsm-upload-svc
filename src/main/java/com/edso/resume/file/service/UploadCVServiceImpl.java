package com.edso.resume.file.service;

import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadCVServiceImpl extends BaseService implements UploadCVService {

    @SneakyThrows
    @Override
    public BaseResponse uploadCV(UploadCVRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        File pdf = new File(String.valueOf(request.getFile()));
        String textParsed;

        PDFParser pdfParser = new PDFParser(new RandomAccessFile(pdf, "r"));
        pdfParser.parse();

        COSDocument cosDocument = pdfParser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = new PDDocument(cosDocument);
        textParsed = pdfTextStripper.getText(pdDocument);

        baseResponse.setSuccess(textParsed);

        return baseResponse;

    }

}
