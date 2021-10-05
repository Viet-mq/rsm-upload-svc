package com.edso.resume.file.domain.entities;

import lombok.Data;

@Data
public class Profile {
    private String id;
    private String fullName;
    private String dateOfBirth;
    private String hometown;
    private String school;
    private String phonenumber;
    private String email;
    private String job;
    private String levelJob;
    private String cv;
    private String sourceCV;
    private String hrRef;
    private String dateOfApply;
    private String cvType;
    private String statusCV;
}
