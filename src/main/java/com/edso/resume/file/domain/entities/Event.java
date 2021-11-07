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
    private Image image;

    public Event(String type, Profile profile) {
        this.type = type;
        this.profile = profile;
    }
}
