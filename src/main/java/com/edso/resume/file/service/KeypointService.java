package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.KeyPoint;
import com.edso.resume.file.domain.request.*;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;

public interface KeypointService {

    GetArrayResponse<KeyPoint> findAll(HeaderInfo headerInfo, String name, Integer page, Integer size);

    BaseResponse createKeypoint(CreateKeypointRequest request);

    BaseResponse updateKeypoint(UpdateKeypointRequest request);

    BaseResponse deleteKeypoint(DeleteKeyPointRequest request);

}
