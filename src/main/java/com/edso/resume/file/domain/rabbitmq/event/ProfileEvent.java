package com.edso.resume.file.domain.rabbitmq.event;

import com.edso.resume.file.domain.entities.Image;
import com.edso.resume.file.domain.entities.Profile;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEvent {
    private String type;
    private Profile profile;
    private Image image;

    public ProfileEvent(String type, Profile profile) {
        this.type = type;
        this.profile = profile;
    }
}
