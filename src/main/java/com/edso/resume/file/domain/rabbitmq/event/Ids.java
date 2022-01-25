package com.edso.resume.file.domain.rabbitmq.event;

import lombok.Data;

@Data
public class Ids {
    private String calendarId;
    private String profileId;
    private String historyId;
}
