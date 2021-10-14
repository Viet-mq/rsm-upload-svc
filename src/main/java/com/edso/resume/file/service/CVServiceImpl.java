package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.Profile;
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
    public void saveCv(Profile profile) {
        cvRepo.save(profile);
    }

    @Override
    public GetArrayResponse<Profile> viewAll(HeaderInfo headerInfo) {
        List<Profile> profiles = cvRepo.findAll();
        GetArrayResponse<Profile> response = new GetArrayResponse<>();
        response.setSuccess(profiles.size(), profiles);
        logger.info("View all: {}" , profiles);
        return response;
    }

    @SneakyThrows
    @Override
    public GetArrayResponse<Profile> viewByKey(HeaderInfo headerInfo, String key) {
        List<Profile> profiles = cvRepo.multiMatchQuery(key);
        GetArrayResponse<Profile> response = new GetArrayResponse<>();
        response.setSuccess(profiles.size(), profiles);
        logger.info("View by key {}: {}", key, profiles);
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
        if (event.getProfile().getId() != null) {
            Profile eventProfile = event.getProfile();
            Profile profile = cvRepo.searchById(event.getProfile().getId());
            if (profile != null) {
                UpdateCVRequest request = new UpdateCVRequest();
                request.setId(profile.getId());
                request.setFullName(eventProfile.getFullName());
                request.setPhoneNumber(eventProfile.getPhoneNumber());
                request.setEmail(eventProfile.getEmail());
                request.setDateOfApply(eventProfile.getDateOfApply());
                request.setHometown(eventProfile.getHometown());
                request.setSchoolId(eventProfile.getSchoolId());
                request.setSchoolName(eventProfile.getSchoolName());
                request.setJobId(eventProfile.getJobId());
                request.setJobName(eventProfile.getJobName());
                request.setLevelJobId(eventProfile.getLevelJobId());
                request.setLevelJobName(eventProfile.getLevelJobName());
                request.setCv(eventProfile.getCv());
                request.setSourceCVId(eventProfile.getSourceCVId());
                request.setSourceCVName(eventProfile.getSourceCVName());
                request.setHrRef(eventProfile.getHrRef());
                request.setDateOfBirth(eventProfile.getDateOfBirth());
                request.setCvType(eventProfile.getCvType());
                request.setStatusCVId(eventProfile.getStatusCVId());
                request.setStatusCVName(eventProfile.getStatusCVName());
                cvRepo.update(request);
                logger.info("Update Profile id: {}", profile.getId());
            }
            else logger.info("Update Profile failed: Invalid id");
        }
    }

    @SneakyThrows
    @Override
    public void delete(String profileId) {
        Profile profile = cvRepo.searchById(profileId);
        if (profile != null) {
            DeleteCVRequest deleteCVRequest = new DeleteCVRequest();
            deleteCVRequest.setId(profile.getId());
            cvRepo.delete(deleteCVRequest);
            logger.info("Delete Profile id: {}", profile.getId());
        }
        else logger.info("Delete Profile failed: Invalid id");
    }

    @SneakyThrows
    @Override
    public void create(Event event) {
        Profile profile = event.getProfile();
        if(cvRepo.searchById(profile.getId()) != null) {
            logger.info("Create Profile failed: ID already exists");
            return;
        }
        cvRepo.save(profile);
        logger.info("Create Profile id : {}", profile.getId());
    }

    @SneakyThrows
    @Override
    public void updateStatus(Event event) {
        Profile profile = cvRepo.searchById(event.getProfile().getId());
        if(profile != null){
            cvRepo.updateStatus(event.getProfile().getId(), event.getProfile().getStatusCVId(), event.getProfile().getStatusCVName());
            logger.info("Update Profile status id: {}, statusId {}, statusName {}", profile.getId()
                    , event.getProfile().getStatusCVId()
                    , event.getProfile().getStatusCVName());
        }
        else logger.info("Update status failed: Invalid ID");
    }
}
