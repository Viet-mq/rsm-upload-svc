package com.edso.resume.file.domain.email;

import com.edso.resume.lib.response.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Component
public class EmailSender {
    private final JavaMailSender javaMailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public BaseResponse sendMail(String toEmail, String subject, String message, List<MultipartFile> files) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if (files != null) {
            for (MultipartFile file : files)
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        javaMailSender.send(mimeMessage);
        baseResponse.setSuccess("OK");

        return baseResponse;
    }

    public BaseResponse sendMail(List<String> toEmails, String subject, String message, List<MultipartFile> files) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();

        for (String toEmail : toEmails) {
            sendMail(toEmail, subject, message, files);
        }

        baseResponse.setSuccess("OK");
        return baseResponse;
    }
}
