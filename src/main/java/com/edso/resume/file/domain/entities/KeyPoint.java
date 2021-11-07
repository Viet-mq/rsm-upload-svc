package com.edso.resume.file.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyPoint {
    private String id;
    private String description;
}
