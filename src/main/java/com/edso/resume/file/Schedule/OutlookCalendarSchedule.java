package com.edso.resume.file.Schedule;

import com.edso.resume.file.domain.entities.OutlookSessionEntity;
import com.edso.resume.file.domain.repo.SessionRepository;
import com.edso.resume.file.publisher.CVPublisher;
import com.edso.resume.lib.http.HttpSender;
import com.edso.resume.lib.http.HttpSenderImpl;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OutlookCalendarSchedule {

    private final SessionRepository sessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(CVPublisher.class);
    @Value("${refresh-token.url}")
    private String url;
    @Value("${refresh-token.client_id}")
    private String clientId;
    @Value("${refresh-token.scope}")
    private String scope;
    @Value("${refresh-token.refresh_token}")
    private String refreshToken;
    @Value("${refresh-token.redirect_uri}")
    private String redirectUri;
    @Value("${refresh-token.grant_type}")
    private String grantType;
    @Value("${refresh-token.client_secret}")
    private String clientSecret;

    public OutlookCalendarSchedule(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(fixedRate = 3000000)
    public void refreshTokenOutlook() {

        HttpSender sender1 = new HttpSenderImpl();
        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("scope", scope);
        body.put("refresh_token", refreshToken);
        body.put("redirect_uri", redirectUri);
        body.put("grant_type", grantType);
        body.put("client_secret", clientSecret);

        try {

            int index = 0;
            JsonObject object = null;

            while (index < 10 && object == null) {
                object = sender1.postForm(url, null, body);
                Thread.sleep(3000);
                index++;
            }

            if (object == null) {
                return;
            }

            if (!object.has("access_token") || object.get("access_token").isJsonNull()) {
                return;
            }

            String token = object.get("access_token").toString().replace("\"", "");

            OutlookSessionEntity outlookSessionEntity = OutlookSessionEntity.builder()
                    .id("outlook")
                    .token(token)
                    .build();

            sessionRepository.addSession(outlookSessionEntity);
            logger.info("refreshTokenOutlook outlookSessionEntity: {}", outlookSessionEntity);

        } catch (Throwable ex) {
            logger.error("Ex: ", ex);
        }

    }

}
