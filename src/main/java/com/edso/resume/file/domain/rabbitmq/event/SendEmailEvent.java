package com.edso.resume.file.domain.rabbitmq.event;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailEvent {
    private String type;
    private String profileId;
    private String templateId;
    private String subject;
    private String content;
    private String historyId;
    List<MultipartFile> files;

    @Override
    public String toString(){
        return "{" +
                "\"type\":\"" + type + '\"' +
                ", \"profileId\":\"" + profileId + '\"' +
                ", \"templateId\":\"" + templateId + '\"' +
                ", \"subject\":\"" + subject + '\"' +
                ", \"content\":\"" + content + '\"' +
                ", \"historyId\":\"" + historyId + '\"' +
                ", \"files\":\"" + files + '\"' +
                '}';
    }
}
