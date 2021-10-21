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
    public GetArrayResponse<Profile> viewAll(HeaderInfo headerInfo, Integer page, Integer size) {
        List<Profile> profiles = cvRepo.findAll();
        GetArrayResponse<Profile> response = new GetArrayResponse<>();
        response.setSuccess(profiles.size(), profiles);
        return response;
    }

    @SneakyThrows
    @Override
    public GetArrayResponse<Profile> viewByKey(HeaderInfo headerInfo, String key, Integer page, Integer size) {
        List<Profile> profiles = cvRepo.multiMatchQuery(key);
        GetArrayResponse<Profile> response = new GetArrayResponse<>();
        response.setSuccess(profiles.size(), profiles);
        return response;
    }

    @SneakyThrows
    @Override
    public BaseResponse update(UpdateCVRequest updateCVRequest){
        BaseResponse response = new BaseResponse();
        Profile profile = cvRepo.searchById(updateCVRequest.getId());
        if(profile == null) {
            response.setFailed("Id không tồn tại");
            return response;
        }
        String result = cvRepo.update(updateCVRequest);
        response.setSuccess(result);
        return response;
    }

    @SneakyThrows
    @Override
    public BaseResponse delete(DeleteCVRequest deleteCVRequest) {
        BaseResponse response = new BaseResponse();
        Profile profile = cvRepo.searchById(deleteCVRequest.getId());
        if(profile == null) {
            response.setFailed("Id không tồn tại");
            return response;
        }
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
                request.setFullName(eventProfile.getFullName()!=null?eventProfile.getFullName(): profile.getFullName());
                request.setPhoneNumber(eventProfile.getPhoneNumber()!=null?eventProfile.getPhoneNumber(): profile.getPhoneNumber());
                request.setEmail(eventProfile.getEmail()!=null?eventProfile.getEmail(): profile.getEmail());
                request.setDateOfApply(eventProfile.getDateOfApply()!=null?eventProfile.getDateOfApply(): profile.getDateOfApply());
                request.setHometown(eventProfile.getHometown()!=null?eventProfile.getHometown(): profile.getHometown());
                request.setSchoolId(eventProfile.getSchoolId()!=null?eventProfile.getSchoolId(): profile.getSchoolId());
                request.setSchoolName(eventProfile.getSchoolName()!=null?eventProfile.getSchoolName(): profile.getSchoolName());
                request.setJobId(eventProfile.getJobId()!=null?eventProfile.getJobId(): profile.getJobId());
                request.setJobName(eventProfile.getJobName()!=null?eventProfile.getJobName(): profile.getJobName());
                request.setLevelJobId(eventProfile.getLevelJobId()!=null?eventProfile.getLevelJobId(): profile.getLevelJobId());
                request.setLevelJobName(eventProfile.getLevelJobName()!=null?eventProfile.getLevelJobName(): profile.getLevelJobName());
                request.setCv(eventProfile.getCv()!=null?eventProfile.getCv(): profile.getCv());
                request.setSourceCVId(eventProfile.getSourceCVId()!=null?eventProfile.getSourceCVId(): profile.getSourceCVId());
                request.setSourceCVName(eventProfile.getSourceCVName()!=null?eventProfile.getSourceCVName(): profile.getSourceCVName());
                request.setHrRef(eventProfile.getHrRef()!=null?eventProfile.getHrRef(): profile.getHrRef());
                request.setDateOfBirth(eventProfile.getDateOfBirth()!=null?eventProfile.getDateOfBirth(): profile.getDateOfBirth());
                request.setCvType(eventProfile.getCvType()!=null?eventProfile.getCvType(): profile.getCvType());
                cvRepo.update(request);
                logger.info("=>Update Profile id: {}", profile.getId());
            }
            else logger.info("=>Update Profile failed: Invalid id");
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
            logger.info("=>Delete Profile id: {}", profile.getId());
        }
        else logger.info("=>Delete Profile failed: Invalid id");
    }

    @SneakyThrows
    @Override
    public void create(Event event) {
        Profile profile = event.getProfile();
        if(cvRepo.searchById(profile.getId()) != null) {
            logger.info("=>Create Profile failed: ID already exists");
            return;
        }
        cvRepo.save(profile);
        logger.info("=>Create Profile id : {}", profile.getId());
    }

    @SneakyThrows
    @Override
    public void updateStatus(Event event) {
        Profile profile = cvRepo.searchById(event.getProfile().getId());
        if(profile != null){
            cvRepo.updateStatus(event.getProfile().getId(), event.getProfile().getStatusCVId(), event.getProfile().getStatusCVName());
            logger.info("=>Update Profile status id: {}, statusId {}, statusName {}", profile.getId()
                    , event.getProfile().getStatusCVId()
                    , event.getProfile().getStatusCVName());
        }
        else logger.info("=>Update status failed: Invalid ID");
    }
}
