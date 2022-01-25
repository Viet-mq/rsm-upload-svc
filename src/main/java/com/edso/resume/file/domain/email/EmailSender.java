package com.edso.resume.file.domain.email;

import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.ErrorCodeDefs;
import com.edso.resume.lib.response.BaseResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public BaseResponse sendMail(String toEmail, String subject, String content, List<String> files) throws MessagingException {
        BaseResponse response = new BaseResponse();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            InternetAddress emailAddress = new InternetAddress(toEmail);
            emailAddress.validate();
        }catch (AddressException e) {
            e.printStackTrace();
            response.setFailed("Không tồn tại tài khoản email " + toEmail);
            return response;
        }


        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, AppUtils.EMAIL_ENCODING);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        if (files.size() != 0) {
            for (String file : files) {
//                URL url = new URL(file);
//                String fileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
//                MultipartFile multipartFile = new MockMultipartFile(url.getFile(), fileName, "text/plain", IOUtils.toByteArray(url));
//                helper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
                try {
                    File fileAttach = new File(file);
                    FileInputStream inputStream = new FileInputStream(fileAttach);
                    MultipartFile multipartFile = new MockMultipartFile(fileAttach.getName(),
                            fileAttach.getName(),
                            "text/plain",
                            IOUtils.toByteArray(inputStream));
                    helper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        javaMailSender.send(mimeMessage);

        response.setSuccess("OK");
        return response;
    }

    public BaseResponse sendMailWithMultipartFile(String toEmail, String subject, String content, List<MultipartFile> files) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, AppUtils.EMAIL_ENCODING);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        if (files != null) {
            for (MultipartFile file : files)
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        javaMailSender.send(mimeMessage);

        return new BaseResponse(ErrorCodeDefs.ERROR_CODE_OK, "OK");
    }

    public BaseResponse sendMail(List<String> toEmails, String subject, String message, List<String> files) throws MessagingException, IOException {

        for (String toEmail : toEmails) {
            sendMail(toEmail, subject, message, files);
        }

        return new BaseResponse(ErrorCodeDefs.ERROR_CODE_OK, "OK");
    }
}
