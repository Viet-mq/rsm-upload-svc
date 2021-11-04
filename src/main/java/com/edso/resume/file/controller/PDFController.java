package com.edso.resume.file.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequestMapping("/pdf")
public class PDFController extends BaseController {

    @Value("${pdf.localpath}")
    private String pdfFilesPath;

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource resource = new UrlResource(Paths.get(pdfFilesPath)
                .resolve(filename)
                .toUri());

        if (resource.exists() || resource.isReadable()) {
            String contentDisposition = String.format("inline; filename=\"%s\"", resource.getFile()
                    .getName());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .contentLength(resource.getFile().length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        }

        return ResponseEntity.notFound()
                .build();
    }

}
