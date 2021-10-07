package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.repo.CvRepo;
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
import java.util.UUID;

@Service
public class UploadCVServiceImpl extends BaseService implements UploadCVService {

    @Autowired
    FileService fileService;

    @Autowired
    CVService cvService;

    @Autowired
    CvRepo cvRepo;

    @SneakyThrows
    @Override
    public BaseResponse uploadCV(UploadCVRequest request) {
        String textParsed;
        BaseResponse baseResponse = new BaseResponse();
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

        CV cv = cvRepo.searchById(request.getProfileId());
        if (cv != null) {
            cv.setContent(textParsed);
            cvRepo.save(cv);
            baseResponse.setSuccess("OK");
        } else
            baseResponse.setFailed("Invalid profileId");

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
