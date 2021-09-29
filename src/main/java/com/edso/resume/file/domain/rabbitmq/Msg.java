package com.edso.resume.file.domain.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Msg.class)
public class Msg implements Serializable {

    private String id;
    private String message;

    public Msg(String id, String userName) {
        this.id = id;
        this.message = userName;
    }

    public Msg() {

    }

    @Override
    public String toString() {
        return "Msg{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}