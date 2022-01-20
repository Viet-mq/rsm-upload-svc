package com.edso.resume.file.service;

import com.edso.resume.lib.response.BaseResponse;

import java.util.List;

public interface SendEmailService {
    BaseResponse sendEmail(String profileId,
                           String subject,
                           String content,
                           String historyId,
                           List<String> files);

    BaseResponse sendMail(String calendarId,
                          String subject,
                          String content,
                          String historyId,
                          List<String> files);
}
