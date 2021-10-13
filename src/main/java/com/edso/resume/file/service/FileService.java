package com.edso.resume.file.service;

import java.io.File;
import java.io.IOException;

public interface FileService {

    public String PdfToText(File pdfFile) throws IOException;
    public String DocxToText(File docxFile);
    public String XlsxToText(File xlsxFile);
}
