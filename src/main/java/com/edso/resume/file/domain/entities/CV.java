package com.edso.resume.file.domain.entities;

import lombok.Data;

@Data
public class CV {
    private String id;
    private String name;
    private String profileId;
    private String pathFile;
    private String content;
}
