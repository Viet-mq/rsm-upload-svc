package com.edso.resume.file.service;

import com.edso.resume.file.domain.db.MongoDbOnlineSyncActions;
import com.edso.resume.file.domain.entities.Interviewer;
import com.edso.resume.file.domain.repo.SessionRepository;
import com.edso.resume.file.domain.request.SendCalendarRequest;
import com.edso.resume.file.utils.SendEmailUtils;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.common.CollectionNameDefs;
import com.edso.resume.lib.common.DbKeyConfig;
import com.edso.resume.lib.http.HttpSender;
import com.edso.resume.lib.http.HttpSenderImpl;
import com.edso.resume.lib.response.BaseResponse;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("SendOutlookCalendarService")
public class SendOutlookCalendarService extends BaseService implements CalendarService {

    @Value("${send-outlook.url}")
    private String url;

    private final SessionRepository sessionRepository;
    private final MongoDbOnlineSyncActions db;

    public SendOutlookCalendarService(SessionRepository sessionRepository, MongoDbOnlineSyncActions db) {
        this.sessionRepository = sessionRepository;
        this.db = db;
    }

    @Override
    public BaseResponse sendCalendar(SendCalendarRequest request) {

        BaseResponse response = new BaseResponse();

        try {

            List<Interviewer> interviewers = new ArrayList<>();
            Document calendar = db.findOne(CollectionNameDefs.COLL_CALENDAR_PROFILE, Filters.eq(DbKeyConfig.ID, request.getCalendarId()));
            if (calendar == null) {
                response.setFailed("Không tồn tại calendar id này");
                return response;
            }

            if (SendEmailUtils.checkInterviewersExistence(response, interviewers, calendar))
                return response;

            String interviewer = "    {\n" +
                    "      \"emailAddress\": {\n" +
                    "        \"address\":\"" + interviewers.get(0).getEmail() + "\",\n" +
                    "        \"name\": \"" + interviewers.get(0).getName() + "\"\n" +
                    "      },\n" +
                    "      \"type\": \"required\"\n" +
                    "    }\n";

            if (interviewers.size() >= 2) {
                for (int i = 1; i < interviewers.size(); i++) {
                    interviewer = interviewer + ",    {\n" +
                            "      \"emailAddress\": {\n" +
                            "        \"address\":\"" + interviewers.get(i).getEmail() + "\",\n" +
                            "        \"name\": \"" + interviewers.get(i).getName() + "\"\n" +
                            "      },\n" +
                            "      \"type\": \"required\"\n" +
                            "    }\n";
                }
            }

            String query = "{\n" +
                    "  \"subject\": \"" + request.getSubject() + "\",\n" +
                    "  \"body\": {\n" +
                    "    \"contentType\": \"HTML\",\n" +
                    "    \"content\": \"" + request.getContent() + "\"\n" +
                    "  },\n" +
                    "  \"start\": {\n" +
                    "      \"dateTime\": \"" + AppUtils.formatDateToString(new Date(AppUtils.parseLong(calendar.get(DbKeyConfig.DATE))), "yyyy-MM-dd'T'HH:mm:ss") + "\",\n" +
                    "      \"timeZone\": \"Asia/Bangkok\"\n" +
                    "  },\n" +
                    "  \"end\": {\n" +
                    "      \"dateTime\": \"" + AppUtils.formatDateToString(new Date(AppUtils.parseLong(calendar.get(DbKeyConfig.INTERVIEW_TIME))), "yyyy-MM-dd'T'HH:mm:ss") + "\",\n" +
                    "      \"timeZone\": \"Asia/Bangkok\"\n" +
                    "  },\n" +
                    "  \"location\":{\n" +
                    "      \"displayName\":\"" + AppUtils.parseString(calendar.get(DbKeyConfig.INTERVIEW_ADDRESS_NAME)) + "\"\n" +
                    "  },\n" +
                    "  \"attendees\": [\n" + interviewer + "  ]\n" +
                    "}";

            JsonObject params = new JsonParser().parse(query).getAsJsonObject();

            HttpSender sender = new HttpSenderImpl();
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + sessionRepository.getToken("outlook"));

            JsonObject result = null;
            int index = 0;

            while (index < 10 && result == null) {
                result = sender.postJson(url, headers, params);
                Thread.sleep(3000);
                index++;
            }

            if (result != null) {
                response.setSuccess();
                return response;
            }

            response.setFailed("Không thể gửi lịch đến đội đồng tuyển dụng!");
            return response;

        } catch (Throwable ex) {
            logger.error("Exception: ", ex);
            response.setFailed("Hệ thống bận");
            return response;
        }

    }

}