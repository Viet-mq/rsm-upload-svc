package com.edso.resume.file.controller;

import com.edso.resume.file.service.EmailService;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.edso.resume.lib.utils.ParseHeaderUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {

    @Autowired
    EmailService emailService;

    @SneakyThrows
    @PostMapping("/send")
    public BaseResponse sendMail(@RequestHeader Map<String, String> headers,
                                 @RequestParam("toEmail") String toEmail,
                                 @RequestParam("subject") String subject,
                                 @RequestParam("content") String content,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>u: {} Send email to: {}", headerInfo, toEmail);
        return emailService.sendMail(toEmail, subject, content, file);
    }

    @PostMapping("/send-template")
    public BaseResponse sendEmailTemplate(@RequestHeader Map<String, String> headers,
                                          @RequestParam String profileId,
                                          @RequestParam String templateId,
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        HeaderInfo headerInfo = ParseHeaderUtil.build(headers);
        logger.info("=>u: {} Send email-template, profileId: {}, templateId: {}", headerInfo, profileId, templateId);
        return emailService.sendMail(headerInfo, profileId, templateId, file);
    }
}
