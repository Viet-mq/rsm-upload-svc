package com.edso.resume.file.domain.entities;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String type;
    private Profile profile;
}
