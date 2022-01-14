package com.edso.resume.file.service;

import com.edso.resume.lib.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SendEmailService {
    BaseResponse sendEmail(String profileId,
                           String subject,
                           String content,
                           String historyId,
                           List<MultipartFile> files);

    BaseResponse sendMail(List<String> toEmails,
                          String subject,
                          String content,
                          String historyId,
                          List<MultipartFile> files);
}
