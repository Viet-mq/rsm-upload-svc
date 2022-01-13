package com.edso.resume.file.service;

import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SendEmailService {
    BaseResponse sendEmail(HeaderInfo headerInfo,
                           String profileId,
                           String templateId,
                           String content,
                           List<MultipartFile> files);
}
