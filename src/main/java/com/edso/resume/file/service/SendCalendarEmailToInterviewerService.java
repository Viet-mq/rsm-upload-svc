package com.edso.resume.file.service;

import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SendCalendarEmailToInterviewerService implements SendEmailService {
    @Override
    public BaseResponse sendEmail(HeaderInfo headerInfo, String profileId, String templateId, String content, List<MultipartFile> file) {
        return null;
    }
}
