package com.edso.resume.file.domain.rabbitmq.event;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailEvent {
    private String type;
    private String profileId;
    private String subject;
    private String content;
    private String historyId;
    private Ids ids;
    List<String> files;
}
