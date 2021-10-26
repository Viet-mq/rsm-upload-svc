package com.edso.resume.file.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Email {
    private String id;
    private String name;
    private String subject;
    private String attachment;
    private String content;
}
