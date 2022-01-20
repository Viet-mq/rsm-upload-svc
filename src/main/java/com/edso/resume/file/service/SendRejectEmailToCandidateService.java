package com.edso.resume.file.service;

import com.edso.resume.file.config.EmailTemplateConfig;
import com.edso.resume.file.config.KeyPointConfig;
import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.email.EmailSender;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.common.DbKeyConfig;
import com.edso.resume.lib.response.BaseResponse;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.SneakyThrows;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendRejectEmailToCandidateService extends BaseService implements SendEmailService {

    BaseResponse response = new BaseResponse();

    private final EmailSender emailSender;

    private final MongoDbOnlineSyncActions db;

    public SendRejectEmailToCandidateService(EmailSender emailSender, MongoDbOnlineSyncActions db) {
        this.emailSender = emailSender;
        this.db = db;
    }

    @SneakyThrows
    @Override
    public BaseResponse sendEmail(String profileId,
                                  String subject,
                                  String content,
                                  String historyId,
                                  List<String> files) {

        Bson cond = Filters.eq(EmailTemplateConfig.ID, profileId);
        Document profile = db.findOne(CollectionNameDefs.COLL_PROFILE, cond);
        if (profile == null) {
            response.setFailed("Profile không tồn tại");
            return response;
        }

        cond = Filters.eq(EmailTemplateConfig.ID_PROFILE, profileId);
        Document reject_profile = db.findOne(CollectionNameDefs.COLL_REASON_REJECT_PROFILE, cond);
        if (reject_profile == null) {
            response.setFailed("Không tồn tại thông tin người dùng trong danh sách từ chối");
            return response;
        }

        Bson historyCond = Filters.eq(EmailTemplateConfig.ID, historyId);
        Document history_email = db.findOne(CollectionNameDefs.COLL_HISTORY_EMAIL, cond);
        if (history_email == null) {
            response.setFailed("Không tồn tại lịch sử gửi mail");
            return response;
        }

        //Name Filter
        String fullName = AppUtils.parseString(profile.get(DbKeyConfig.FULL_NAME));
        String lastName;
        String firstName = null;
        if (fullName.split("\\w+").length > 1) {
            firstName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            lastName = fullName.substring(0, fullName.indexOf(" "));
        } else {
            lastName = fullName;
        }

        //Get all keypoint in content and subject
        final Pattern pattern = Pattern.compile(AppUtils.KEYPOINT_PATTERN);
        Matcher keypointMatcher = pattern.matcher(content);
        final List<String> keypointList = new LinkedList<>();
        while (keypointMatcher.find()) {
            keypointList.add(keypointMatcher.group(1));
        }

        keypointMatcher = pattern.matcher(subject);
        while (keypointMatcher.find()) {
            keypointList.add(keypointMatcher.group(1));
        }

        Map<String, String> replacementStrings = new HashMap<>();
        for (String placeholder : keypointList) {
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
                    break;
                case KeyPointConfig.ROUND:
                    replacementStrings.put(KeyPointConfig.ROUND, AppUtils.parseString(reject_profile.get(DbKeyConfig.STATUS_CV_NAME)));
                    break;
                case KeyPointConfig.REJECT_REASON:
                    replacementStrings.put(KeyPointConfig.REJECT_REASON, AppUtils.parseString(reject_profile.get(DbKeyConfig.REASON)));
                    break;
            }
        }

        //Replace keypoint
        StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
        String contentResult = sub.replace(content);
        String subjectResult = sub.replace(subject);

        //Update Email's history
        Bson updates = Updates.combine(
                Updates.set(DbKeyConfig.STATUS, "Đã gửi email")
        );
        db.update(CollectionNameDefs.COLL_HISTORY_EMAIL, historyCond, updates, true);

        return emailSender.sendMail(AppUtils.parseString(profile.get(DbKeyConfig.EMAIL)),
                subjectResult, contentResult, files);
    }

    @Override
    public BaseResponse sendMail(String calendarId, String subject, String content, String historyId, List<String> files) {
        return null;
    }
}
