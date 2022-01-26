package com.edso.resume.file.utils;

import com.edso.resume.file.domain.entities.Interviewer;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.DbKeyConfig;
import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import org.bson.Document;

import java.util.List;

public class SendEmailUtils {

    @SuppressWarnings("unchecked")
    public static boolean checkInterviewersExistence(BaseResponse response, List<Interviewer> interviewers, Document calendar) {
        List<Document> interviewerList = (List<Document>) calendar.get(DbKeyConfig.INTERVIEWERS);

        if (interviewerList == null || interviewerList.isEmpty()) {
            response.setFailed("Không tồn tại hội đồng tuyển dụng");
            return true;
        }

        for (Document document : interviewerList) {
            if (!Strings.isNullOrEmpty(AppUtils.parseString(document.get(DbKeyConfig.EMAIL)))) {
                Interviewer interviewer = Interviewer.builder()
                        .name(AppUtils.parseString(document.get(DbKeyConfig.FULL_NAME)))
                        .email(AppUtils.parseString(document.get(DbKeyConfig.EMAIL)))
                        .build();
                interviewers.add(interviewer);
            }
        }

        if (interviewers.isEmpty()) {
            response.setFailed("Không tồn tại hội đồng tuyển dụng");
            return true;
        }
        return false;
    }
}
