package com.edso.resume.file.service;

import com.edso.resume.file.domain.elasticsearch.ElasticSearchActions;
import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class UploadCVServiceImpl extends BaseService implements UploadCVService {

    @Autowired
    FileService fileService;

    @SneakyThrows
    @Override
    public BaseResponse uploadCV(UploadCVRequest request) {
        String textParsed;
        BaseResponse baseResponse = new BaseResponse();
        ElasticSearchActions elastic = new ElasticSearchActions();
        MultipartFile fileUpload = request.getFile();
        File file = convertToFile(fileUpload);
        String extension = Objects.requireNonNull(fileUpload
                        .getOriginalFilename())
                        .substring(fileUpload.getOriginalFilename()
                        .lastIndexOf(".") + 1);

        switch (extension){
            case "pdf":
                textParsed = fileService.PdfToText(file);
                break;
            case "docx":
                textParsed = fileService.DocxToText(file);
                break;
            case "xlsx":
                textParsed = fileService.XlsxToText(file);
                break;
            default:
                baseResponse.setFailed("Invalid file");
                return baseResponse;
        }

        baseResponse.setSuccess("OK");
        //elastic.insertTextIntoElasticsearch(textParsed);

        return baseResponse;

    }

    public static File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getName());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
