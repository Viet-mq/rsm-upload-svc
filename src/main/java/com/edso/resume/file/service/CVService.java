package com.edso.resume.file.service;

import com.edso.resume.file.domain.rabbitmq.event.ProfileEvent;
import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;

public interface CVService {

    void saveCv(Profile profile);

    void update(ProfileEvent profileEvent);

    void delete(String profileId);

    void create(ProfileEvent profileEvent);

    void updateStatus(ProfileEvent profileEvent);

    void updateImages(ProfileEvent profileEvent);

    void deleteImages(ProfileEvent profileEvent);

    GetArrayResponse<Profile> viewAll(HeaderInfo headerInfo);

    GetArrayResponse<Profile> viewByKey(HeaderInfo headerInfo, String key, Integer size);

    BaseResponse delete(DeleteCVRequest deleteCVRequest);

    BaseResponse update(UpdateCVRequest updateCVRequest);
}
