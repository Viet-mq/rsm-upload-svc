package com.edso.resume.file.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Interviewer {
    private String name;
    private String email;
}
