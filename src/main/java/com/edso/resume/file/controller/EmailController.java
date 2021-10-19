package com.edso.resume.file.controller;

import com.edso.resume.file.service.EmailService;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {

    @Autowired
    EmailService emailService;

    @SneakyThrows
    @PostMapping("/send")
    public BaseResponse sendMail(@RequestParam("toEmail") String toEmail,
                                 @RequestParam("subject") String subject,
                                 @RequestParam("message") String message,
                                 @RequestParam(value = "file", required = false) MultipartFile file) {
        logger.info("=>Send email to: {}" , toEmail);
        return emailService.sendMail(toEmail, subject, message, file);
    }
}
