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
        CV cv = cvRepo.searchByProfileId(event.getCv().getProfileId());
        if (cv != null) {
            UpdateCVRequest updateCVRequest = new UpdateCVRequest();
            updateCVRequest.setId(cv.getId());
            updateCVRequest.setName(event.getCv().getName());
            updateCVRequest.setProfileId(event.getCv().getProfileId());
            updateCVRequest.setPathFile(event.getCv().getPathFile());
            updateCVRequest.setContent(event.getCv().getContent());
            cvRepo.update(updateCVRequest);
        }
    }

    @SneakyThrows
    @Override
    public void delete(String profileId) {
        CV cv = cvRepo.searchByProfileId(profileId);
        if (cv != null) {
            DeleteCVRequest deleteCVRequest = new DeleteCVRequest();
            deleteCVRequest.setId(cv.getId());
            cvRepo.delete(deleteCVRequest);
        }
    }
}
