package com.edso.resume.file.service;

import com.edso.resume.file.domain.repo.SessionRepository;
import com.edso.resume.file.domain.request.SendCalendarRequest;
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

@Service("SendOutlookCalendarService")
public class SendOutlookCalendarService extends BaseService implements CalendarService {

    @Value("${send-outlook.url}")
    private String url;
    private final SessionRepository sessionRepository;

    public SendOutlookCalendarService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public BaseResponse sendCalendar(SendCalendarRequest request) {

        BaseResponse response = new BaseResponse();

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

            JsonObject params = new JsonParser().parse(query1).getAsJsonObject();

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