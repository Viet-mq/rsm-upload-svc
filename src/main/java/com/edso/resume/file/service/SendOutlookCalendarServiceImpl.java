package com.edso.resume.file.service;

import com.edso.resume.file.domain.repo.SessionRepository;
import com.edso.resume.file.domain.request.SendOutlookCalendarRequest;
import com.edso.resume.lib.common.AppUtils;
import com.edso.resume.lib.http.HttpSender;
import com.edso.resume.lib.http.HttpSenderImpl;
import com.edso.resume.lib.response.BaseResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendOutlookCalendarServiceImpl extends BaseService implements SendOutlookCalendarService {

    private final SessionRepository sessionRepository;
    @Value("${send-outlook.url}")
    private String url;

    public SendOutlookCalendarServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public BaseResponse sendOutlookCalendar(SendOutlookCalendarRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String query1 = "{\n" +
                    "  \"subject\": \"" + request.getSubject() + "\",\n" +
                    "  \"body\": {\n" +
                    "    \"contentType\": \"HTML\",\n" +
                    "    \"content\": \"" + request.getContent() + "\"\n" +
                    "  },\n" +
                    "  \"start\": {\n" +
                    "      \"dateTime\": \"" + AppUtils.formatDateToString(new Date(request.getStartTime()), "yyyy-MM-dd'T'HH:mm:ss") + "\",\n" +
                    "      \"timeZone\": \"Asia/Bangkok\"\n" +
                    "  },\n" +
                    "  \"end\": {\n" +
                    "      \"dateTime\": \"" + AppUtils.formatDateToString(new Date(request.getEndTime()), "yyyy-MM-dd'T'HH:mm:ss") + "\",\n" +
                    "      \"timeZone\": \"Asia/Bangkok\"\n" +
                    "  },\n" +
                    "  \"location\":{\n" +
                    "      \"displayName\":\"" + request.getAddress() + "\"\n" +
                    "  },\n" +
                    "  \"attendees\": [\n" +

                    "    {\n" +
                    "      \"emailAddress\": {\n" +
                    "        \"address\":\"quanpk@edsolabs.com\",\n" +
                    "        \"name\": \"quanpham\"\n" +
                    "      },\n" +
                    "      \"type\": \"required\"\n" +
                    "    }\n" +

                    "  ]\n" +
                    "}";

            JsonObject jsonObject = new JsonParser().parse(query1).getAsJsonObject();

            HttpSender sender = new HttpSenderImpl();
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + sessionRepository.getToken("outlook"));
            JsonObject object = sender.postJson(url, headers, jsonObject);
            if(object != null) baseResponse.setSuccess();
            else baseResponse.setFailed("Không thể gửi lịch đến đội đồng tuyển dụng!");
            return baseResponse;

        } catch (Throwable ex) {
            logger.error("Exception: ", ex);
            baseResponse.setFailed("Hệ thống bận");
            return baseResponse;
        }
    }
}