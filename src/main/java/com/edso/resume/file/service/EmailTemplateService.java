package com.edso.resume.file.service;

import com.edso.resume.file.domain.entities.Email;
import com.edso.resume.file.domain.request.CreateEmailTemplateRequest;
import com.edso.resume.file.domain.request.DeleteEmailTemplateRequest;
import com.edso.resume.file.domain.request.UpdateEmailTemplateRequest;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.response.GetArrayResponse;

public interface EmailTemplateService {
    GetArrayResponse<Email> findAll(HeaderInfo headerInfo, String name, Integer page, Integer size);

    BaseResponse createEmailTemplate(CreateEmailTemplateRequest request);

    BaseResponse updateEmailTemplate(UpdateEmailTemplateRequest request);

    BaseResponse deleteEmailTemplate(DeleteEmailTemplateRequest request);
}
