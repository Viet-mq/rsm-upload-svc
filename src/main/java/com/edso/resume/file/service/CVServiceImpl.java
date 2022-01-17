package com.edso.resume.file.service;

import com.edso.resume.file.domain.rabbitmq.event.ProfileEvent;
import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.repo.CvRepo;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return response;
    }

    @SneakyThrows
    @Override
    public GetArrayResponse<Profile> viewByKey(HeaderInfo headerInfo, String key, Integer size) {
        List<Profile> profiles;
        if (key.startsWith("\"")){
            profiles = cvRepo.matchQuery(key, size);
        }
        else if (size != null) {
            profiles = cvRepo.multiMatchQuery(key, size);
        }
        else profiles = cvRepo.multiMatchQuery(key, 10);
        GetArrayResponse<Profile> response = new GetArrayResponse<>();
        response.setSuccess(profiles.size(), profiles);
        return response;
    }

    @SneakyThrows
    @Override
    public BaseResponse update(UpdateCVRequest updateCVRequest) {
        BaseResponse response = new BaseResponse();
        Profile profile = cvRepo.searchById(updateCVRequest.getId());
        if (profile == null) {
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
        if (profile == null) {
            response.setFailed("Id không tồn tại");
            return response;
        }
        String result = cvRepo.delete(deleteCVRequest);
        response.setSuccess(result);
        return response;
    }

    @SneakyThrows
    @Override
    public void update(ProfileEvent profileEvent) {
        if (profileEvent.getProfile().getId() != null) {
            Profile eventProfile = profileEvent.getProfile();
            Profile profile = cvRepo.searchById(profileEvent.getProfile().getId());
            if (profile != null) {
                UpdateCVRequest request = new UpdateCVRequest();
                request.setId(profile.getId());
                request.setFullName(eventProfile.getFullName() != null ? eventProfile.getFullName() : profile.getFullName());
                request.setPhoneNumber(eventProfile.getPhoneNumber() != null ? eventProfile.getPhoneNumber() : profile.getPhoneNumber());
                request.setGender(eventProfile.getGender() != null ? eventProfile.getGender() : profile.getGender());
                request.setEmail(eventProfile.getEmail() != null ? eventProfile.getEmail() : profile.getEmail());
                request.setDateOfApply(eventProfile.getDateOfApply() != null ? eventProfile.getDateOfApply() : profile.getDateOfApply());
                request.setHometown(eventProfile.getHometown() != null ? eventProfile.getHometown() : profile.getHometown());
                request.setSchoolId(eventProfile.getSchoolId() != null ? eventProfile.getSchoolId() : profile.getSchoolId());
                request.setSchoolName(eventProfile.getSchoolName() != null ? eventProfile.getSchoolName() : profile.getSchoolName());
                request.setJobId(eventProfile.getJobId() != null ? eventProfile.getJobId() : profile.getJobId());
                request.setJobName(eventProfile.getJobName() != null ? eventProfile.getJobName() : profile.getJobName());
                request.setLevelJobId(eventProfile.getLevelJobId() != null ? eventProfile.getLevelJobId() : profile.getLevelJobId());
                request.setLevelJobName(eventProfile.getLevelJobName() != null ? eventProfile.getLevelJobName() : profile.getLevelJobName());
                request.setCv(eventProfile.getCv() != null ? eventProfile.getCv() : profile.getCv());
                request.setSourceCVId(eventProfile.getSourceCVId() != null ? eventProfile.getSourceCVId() : profile.getSourceCVId());
                request.setSourceCVName(eventProfile.getSourceCVName() != null ? eventProfile.getSourceCVName() : profile.getSourceCVName());
                request.setHrRef(eventProfile.getHrRef() != null ? eventProfile.getHrRef() : profile.getHrRef());
                request.setDateOfBirth(eventProfile.getDateOfBirth() != null ? eventProfile.getDateOfBirth() : profile.getDateOfBirth());
                request.setCvType(eventProfile.getCvType() != null ? eventProfile.getCvType() : profile.getCvType());
                request.setTalentPoolId(eventProfile.getTalentPoolId() != null ? eventProfile.getTalentPoolId() : profile.getTalentPoolId());
                request.setTalentPoolName(eventProfile.getTalentPoolName() != null ? eventProfile.getTalentPoolName() : profile.getTalentPoolName());
                request.setLastApply(eventProfile.getLastApply() != null ? eventProfile.getLastApply() : profile.getLastApply());
                request.setSchoolLevel(eventProfile.getSchoolLevel() != null ? eventProfile.getSchoolLevel() : profile.getSchoolLevel());
                request.setEvaluation(eventProfile.getEvaluation() != null ? eventProfile.getEvaluation() : profile.getEvaluation());
                request.setDepartmentId(eventProfile.getDepartmentId() != null ? eventProfile.getDepartmentId() : profile.getDepartmentId());
                request.setDepartmentName(eventProfile.getDepartmentName() != null ? eventProfile.getDepartmentName() : profile.getDepartmentName());
                cvRepo.update(request);
                logger.info("=>Update Profile id: {}", profile.getId());
            } else logger.info("=>Update Profile failed: Invalid id");
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
        } else logger.info("=>Delete Profile failed: Invalid id");
    }

    @SneakyThrows
    @Override
    public void create(ProfileEvent profileEvent) {
        Profile profile = profileEvent.getProfile();
        if (profile.getId() == null){
            logger.info("=>Create Profile failed: profile ID is null");
            return;
        }
        if (cvRepo.searchById(profile.getId()) != null) {
            logger.info("=>Create Profile failed: ID already exists");
            return;
        }
        cvRepo.save(profile);
        logger.info("=>Create Profile id : {}", profile.getId());
    }

    @SneakyThrows
    @Override
    public void updateStatus(ProfileEvent profileEvent) {
        if (profileEvent.getProfile().getId() == null){
            logger.info("=>Update status failed: profile ID is null");
            return;
        }
        Profile profile = cvRepo.searchById(profileEvent.getProfile().getId());
        if (profile != null) {
            cvRepo.updateStatus(profileEvent.getProfile().getId(), profileEvent.getProfile().getStatusCVId(), profileEvent.getProfile().getStatusCVName());
            logger.info("=>Update Profile status id: {}, statusId {}, statusName {}", profile.getId()
                    , profileEvent.getProfile().getStatusCVId()
                    , profileEvent.getProfile().getStatusCVName());
        } else logger.info("=>Update status failed: Invalid ID");
    }

    @SneakyThrows
    @Override
    public void updateImages(ProfileEvent profileEvent) {
        if (profileEvent.getImage().getId() == null){
            logger.info("=>Update Image failed: profile ID is null");
            return;
        }
        Profile profile = cvRepo.searchById(profileEvent.getImage().getId());
        if (profile != null) {
            cvRepo.updateImages(profileEvent.getImage().getId(), profileEvent.getImage().getUrl());
            logger.info("=>Update Image to id: {}, url_image: {}", profile.getId(), profileEvent.getImage().getUrl());
        } else logger.info("Update Image failed: Invalid ID");
    }

    @SneakyThrows
    @Override
    public void deleteImages(ProfileEvent profileEvent) {
        if (profileEvent.getImage().getId() == null){
            logger.info("=>Delete Image failed: profile ID is null");
            return;
        }
        Profile profile = cvRepo.searchById(profileEvent.getImage().getId());
        if (profile != null) {
            cvRepo.deleteImages(profileEvent.getImage().getId());
            logger.info("=>Delete Image in id: {}", profile.getId());
        } else logger.info("Delete Image failed: Invalid ID");
    }
}
