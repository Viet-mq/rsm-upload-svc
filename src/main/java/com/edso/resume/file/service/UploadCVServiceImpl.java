package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.repo.CvRepo;
import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.file.domain.rabbitmq.publisher.CVPublisher;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class UploadCVServiceImpl extends BaseService implements UploadCVService {

    @Value("${pdf.domain}")
    private String domain;

    @Value("${pdf.serverpath}")
    private String pdfFilesPath;

    final CVPublisher cvPublisher;

    final FileService fileService;

    final CvRepo cvRepo;

    public UploadCVServiceImpl(CVPublisher cvPublisher, FileService fileService, CvRepo cvRepo) {
        this.cvPublisher = cvPublisher;
        this.fileService = fileService;
        this.cvRepo = cvRepo;
    }

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

        switch (extension) {
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

        Profile profile = cvRepo.searchById(request.getProfileId());
        if (profile != null) {
            profile.setContent(profile.getContent() + textParsed);
            profile.setFileName(fileUpload.getOriginalFilename());
            profile.setUrlCV(domain + fileUpload.getOriginalFilename());
            profile.setCv(fileUpload.getOriginalFilename());
            profile.setCvType(extension);
            CV cv = new CV(request.getProfileId(), extension, profile.getUrlCV(), profile.getFileName());
            cvPublisher.publish(cv);
            cvRepo.save(profile);
        } else {
            Profile profile1 = new Profile();
            profile1.setId(request.getProfileId());
            profile1.setContent(textParsed);
            profile1.setFileName(fileUpload.getOriginalFilename());
            profile1.setUrlCV(domain + fileUpload.getOriginalFilename());
            profile1.setCvType(extension);
            CV cv = new CV(request.getProfileId(), extension, profile1.getUrlCV(), profile1.getFileName());
            cvPublisher.publish(cv);
            cvRepo.saveContent(profile1);
        }
        baseResponse.setSuccess("OK");
        return baseResponse;

    }

    public File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(pdfFilesPath + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
