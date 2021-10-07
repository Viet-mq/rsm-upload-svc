package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.entities.Event;
import com.edso.resume.file.domain.repo.CvRepo;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CVServiceImpl extends BaseService implements CVService {

    private final CvRepo cvRepo;

    public CVServiceImpl(CvRepo cvRepo) {
        this.cvRepo = cvRepo;
    }

    @Override
    public void saveCv(CV cv) {
        cvRepo.save(cv);
    }

    @Override
    public GetArrayResponse<CV> viewAll(HeaderInfo headerInfo) {
        List<CV> cvs = cvRepo.findAll();
        GetArrayResponse<CV> response = new GetArrayResponse<>();
        response.setSuccess(cvs.size(), cvs);
        logger.info("View all: {}" , cvs);
        return response;
    }

    @SneakyThrows
    @Override
    public GetArrayResponse<CV> viewByKey(HeaderInfo headerInfo, String key) {
        List<CV> cvs = cvRepo.multiMatchQuery(key);
        GetArrayResponse<CV> response = new GetArrayResponse<>();
        response.setSuccess(cvs.size(), cvs);
        logger.info("View by key {}: {}", key, cvs);
        return response;
    }

    @SneakyThrows
    @Override
    public BaseResponse update(HeaderInfo header, UpdateCVRequest updateCVRequest){
        BaseResponse response = new BaseResponse();
        String result = cvRepo.update(updateCVRequest);
        response.setSuccess(result);
        return response;
    }

    @Override
    public BaseResponse delete(HeaderInfo headerInfo, DeleteCVRequest deleteCVRequest) {
        BaseResponse response = new BaseResponse();
        String result = cvRepo.delete(deleteCVRequest);
        response.setSuccess(result);
        return response;
    }

    @SneakyThrows
    @Override
    public void update(Event event) {
        if (event.getCv().getId() != null) {
            CV eventCv = event.getCv();
            CV cv = cvRepo.searchById(event.getCv().getId());
            if (cv != null) {
                UpdateCVRequest request = new UpdateCVRequest();
                request.setId(cv.getId());
                request.setFullName(eventCv.getFullName());
                request.setPhoneNumber(eventCv.getPhoneNumber());
                request.setEmail(eventCv.getEmail());
                request.setDateOfApply(eventCv.getDateOfApply());
                request.setHometown(eventCv.getHometown());
                request.setSchool(eventCv.getSchool());
                request.setJob(eventCv.getJob());
                request.setLevelJob(eventCv.getLevelJob());
                request.setCv(eventCv.getCv());
                request.setSourceCV(eventCv.getSourceCV());
                request.setHrRef(eventCv.getHrRef());
                request.setDateOfBirth(eventCv.getDateOfBirth());
                request.setCvType(eventCv.getCvType());
                request.setStatusCV(eventCv.getStatusCV());
                cvRepo.update(request);
                logger.info("Update CV id: {}", cv.getId());
            }
            else logger.info("Update CV failed: Invalid id");
        }
    }

    @SneakyThrows
    @Override
    public void delete(String profileId) {
        CV cv = cvRepo.searchById(profileId);
        if (cv != null) {
            DeleteCVRequest deleteCVRequest = new DeleteCVRequest();
            deleteCVRequest.setId(cv.getId());
            cvRepo.delete(deleteCVRequest);
            logger.info("Delete CV id: {}", cv.getId());
        }
        else logger.info("Delete CV failed: Invalid id");
    }

    @SneakyThrows
    @Override
    public void create(Event event) {
        CV cv = event.getCv();
        if(cvRepo.searchById(cv.getId()) != null) {
            logger.info("Create CV failed: ID already exists");
            return;
        }
        cvRepo.save(cv);
        logger.info("Create CV id : {}", cv.getId());
    }

    @SneakyThrows
    @Override
    public void updateStatus(Event event) {
        CV cv = cvRepo.searchById(event.getCv().getId());
        if(cv != null){
            cvRepo.updateStatus(event.getCv().getId(), event.getCv().getStatusCV());
            logger.info("Update CV status id: {}, value {}", cv.getId(), cv.getStatusCV());
        }
        else logger.info("Update status failed: Invalid ID");
    }
}
