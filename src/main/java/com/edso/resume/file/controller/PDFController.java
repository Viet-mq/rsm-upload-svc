package com.edso.resume.file.controller;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/pdf")
public class PDFController extends BaseController {

    private final String pdfFilesPath = "D:\\";

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
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
