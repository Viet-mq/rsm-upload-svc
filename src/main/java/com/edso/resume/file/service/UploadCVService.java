package com.edso.resume.file.service;

import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.lib.response.BaseResponse;

public interface UploadCVService {
    BaseResponse uploadCV(UploadCVRequest request);
}
