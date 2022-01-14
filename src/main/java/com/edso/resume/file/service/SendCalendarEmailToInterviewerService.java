package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.email.EmailSender;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SendCalendarEmailToInterviewerService implements SendEmailService {

    BaseResponse response = new BaseResponse();

    private final EmailSender emailSender;

    private final MongoDbOnlineSyncActions db;

    public SendCalendarEmailToInterviewerService(EmailSender emailSender, MongoDbOnlineSyncActions db) {
        this.emailSender = emailSender;
        this.db = db;
    }

    @Override
    public BaseResponse sendEmail(String profileId, String subject, String content, String historyId, List<MultipartFile> file) {
        return null;
    }

    @SneakyThrows
    @Override
    public BaseResponse sendMail(List<String> toEmails, String subject, String content, String historyId, List<MultipartFile> files) {

        for (String toEmail : toEmails) {
            emailSender.sendMail(toEmail, subject, content, files);
        }

        response.setSuccess("OK");
        return response;
    }
}
