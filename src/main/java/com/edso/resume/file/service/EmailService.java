package com.edso.resume.file.service;

import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

public interface EmailService {
    public BaseResponse sendMailMultipart(String email, String subject, String message, MultipartFile file) throws MessagingException;

    public BaseResponse sendMail(String toEmail, String subject, String message) throws MessagingException;

    public BaseResponse sendMail(String toEmail, String subject, String message, MultipartFile file) throws MessagingException;

    public BaseResponse sendMail(HeaderInfo headerInfo, String profileId, String templateId, MultipartFile file);
}