package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.entities.Event;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;

import java.util.List;
import java.util.UUID;

public interface CVService {

    void saveCv(CV cv);
    void update(Event event);
    void delete(String profileId);
    void create(Event event);
    GetArrayResponse<CV> viewAll (HeaderInfo headerInfo);
    GetArrayResponse<CV> viewByKey(HeaderInfo headerInfo, String key);
    BaseResponse delete(HeaderInfo headerInfo, DeleteCVRequest deleteCVRequest);
    BaseResponse update(HeaderInfo headerInfo, UpdateCVRequest updateCVRequest);
}
