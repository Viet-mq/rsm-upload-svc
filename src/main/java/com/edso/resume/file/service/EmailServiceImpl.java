package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.KeyPoint;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl extends BaseService implements EmailService{

    private final MongoDbOnlineSyncActions db;

    public EmailServiceImpl (MongoDbOnlineSyncActions db) {
        this.db = db;
    }

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    public BaseResponse sendMailMultipart(String toEmail, String subject, String message, MultipartFile file) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if(file != null){
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        javaMailSender.send(mimeMessage);
        baseResponse.setSuccess("OK");

        return baseResponse;
    }

    public BaseResponse sendMail(String toEmail, String subject, String message) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, (MultipartFile) null);
    }

    public BaseResponse sendMail(String toEmail, String subject, String message, MultipartFile file) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, file);
    }
}
