package com.edso.resume.file.service;

import com.edso.resume.file.config.KeyPointConfig;
import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.common.DbKeyConfig;
import com.edso.resume.lib.entities.HeaderInfo;
import com.edso.resume.lib.response.BaseResponse;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl extends BaseService implements EmailService {

    private final MongoDbOnlineSyncActions db;

    public EmailServiceImpl(MongoDbOnlineSyncActions db) {
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

        if (file != null) {
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        javaMailSender.send(mimeMessage);
        baseResponse.setSuccess("OK");

        return baseResponse;
    }

    public BaseResponse sendMail(String toEmail, String subject, String message) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, null);
    }

    public BaseResponse sendMail(String toEmail, String subject, String message, MultipartFile file) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, file);
    }

    public BaseResponse sendMail(String toEmail, String subject, String message, File file) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);
        helper.addAttachment(file.getName(), file);

        javaMailSender.send(mimeMessage);
        baseResponse.setSuccess("OK");

        return baseResponse;
    }

    @SneakyThrows
    @Override
    public BaseResponse sendMail(HeaderInfo headerInfo, String profileId, String templateId, MultipartFile file) {

        BaseResponse response = new BaseResponse();

        Bson cond = Filters.eq("id", profileId);
        Document profile = db.findOne(CollectionNameDefs.COLL_PROFILE, cond);
        if (profile == null) {
            response.setFailed("Profile không tồn tại");
            return response;
        }

        cond = Filters.eq("id", templateId);
        Document template = db.findOne(CollectionNameDefs.COLL_EMAIL_TEMPLATE, cond);
        if (template == null) {
            response.setFailed("Template không tồn tại");
            return response;
        }

        //Name Filter
        String content = AppUtils.parseString(template.get("content"));
        String fullName = AppUtils.parseString(profile.get(DbKeyConfig.FULL_NAME));
        String lastName;
        String firstName = null;
        if (fullName.split("\\w+").length > 1) {
            firstName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            lastName = fullName.substring(0, fullName.indexOf(" "));
        } else {
            lastName = fullName;
        }

        //Get all keypoint in template's content
        final Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        final Matcher matcher = pattern.matcher(content);
        final List<String> list = new LinkedList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }

        Map<String, String> replacementStrings = new HashMap<>();
        for (String placeholder : list) {
            switch (placeholder) {
                case KeyPointConfig.FULL_NAME:
                    replacementStrings.put(KeyPointConfig.FULL_NAME, fullName);
                    break;
                case KeyPointConfig.NAME:
                case KeyPointConfig.FIRST_NAME:
                    replacementStrings.put(KeyPointConfig.NAME, firstName);
                    break;
                case KeyPointConfig.LAST_NAME:
                    replacementStrings.put(KeyPointConfig.LAST_NAME, lastName);
                    break;
                case KeyPointConfig.EMAIL:
                    replacementStrings.put(KeyPointConfig.EMAIL, AppUtils.parseString(profile.get(DbKeyConfig.EMAIL)));
                    break;
                case KeyPointConfig.JOB:
                    replacementStrings.put(KeyPointConfig.JOB, AppUtils.parseString(profile.get(DbKeyConfig.JOB_NAME)));
                    break;
                case KeyPointConfig.COMPANY:
                    replacementStrings.put(KeyPointConfig.COMPANY, "Edsolabs");
                    break;
                case KeyPointConfig.USER_NAME:
                case KeyPointConfig.USER_FIRSTNAME:
                case KeyPointConfig.ME:
                    replacementStrings.put(KeyPointConfig.USER_NAME, headerInfo.getUsername());
                    break;
                case KeyPointConfig.USER_FULL_NAME:
                    replacementStrings.put(KeyPointConfig.USER_FULL_NAME, headerInfo.getFullName());
                    break;
                case KeyPointConfig.PREFIX:
                    String gender = AppUtils.parseString(profile.get(DbKeyConfig.GENDER));
                    if (gender.equals("Nam"))
                        replacementStrings.put(KeyPointConfig.PREFIX, "Anh");
                    else
                        replacementStrings.put(KeyPointConfig.PREFIX, "Chị");
                    break;
                case KeyPointConfig.USER_EMAIL:
                case KeyPointConfig.USER_PHONE:
                case KeyPointConfig.NOTE:
                case KeyPointConfig.OFFICES:
                case KeyPointConfig.DEPT:
                    replacementStrings.put(KeyPointConfig.DEPT, AppUtils.parseString(profile.get(DbKeyConfig.DEPARTMENT_NAME)));
                case KeyPointConfig.JOB_UPDATE_LINK:
                case KeyPointConfig.JOB_LINK:
                    break;
            }
        }

        //Replace keypoint
        StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
        String result = sub.replace(content);

        return sendMailMultipart(AppUtils.parseString(profile.get(DbKeyConfig.EMAIL)),
                AppUtils.parseString(template.get("subject")),
                result, file);
    }
}
