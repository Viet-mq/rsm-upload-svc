package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.repo.CvRepo;
import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.response.BaseResponse;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class UploadCVServiceImpl extends BaseService implements UploadCVService {

    private final MongoDbOnlineSyncActions db;

    @Value("${pdf.domain}")
    private String domain;

    @Value("${pdf.serverpath}")
    private String pdfFilesPath;

    @Autowired
    FileService fileService;

    @Autowired
    CvRepo cvRepo;

    public UploadCVServiceImpl(MongoDbOnlineSyncActions db) {
        this.db = db;
    }

    @SneakyThrows
    @Override
    public BaseResponse uploadCV(UploadCVRequest request) {
        String textParsed;
        BaseResponse baseResponse = new BaseResponse();

        Bson cond = Filters.eq("id", request.getProfileId());
        Document idDocument = db.findOne(CollectionNameDefs.COLL_PROFILE, cond);

        if(idDocument == null){
            baseResponse.setFailed("Id profile không tồn tại");
            return baseResponse;
        }

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

        Profile profile = cvRepo.searchById(request.getProfileId());
        if (profile != null) {
            profile.setContent(profile.getContent() + textParsed);
            profile.setFileName(fileUpload.getOriginalFilename());
            profile.setUrl(domain + fileUpload.getOriginalFilename());
            cvRepo.save(profile);
        } else {
            Profile profile1 = new Profile();
            profile1.setId(request.getProfileId());
            profile1.setContent(textParsed);
            profile1.setFileName(fileUpload.getOriginalFilename());
            profile1.setUrl(domain + fileUpload.getOriginalFilename());
            cvRepo.saveContent(profile1);
        }

        Bson update = Updates.combine(
                Updates.set("cv", fileUpload.getOriginalFilename()),
                Updates.set("cv_type", extension)
        );
        db.update(CollectionNameDefs.COLL_PROFILE, cond, update, true);

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
