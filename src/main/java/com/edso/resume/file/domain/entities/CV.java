package com.edso.resume.file.domain.entities;

import lombok.Data;

@Data
public class CV {
    private String id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Long dateOfBirth;
    private String hometown;
    private String school;
    private String job;
    private String levelJob;
    private String cv;
    private String sourceCV;
    private String hrRef;
    private Long dateOfApply;
    private String cvType;
    private String statusCV;
    private String content;
}
