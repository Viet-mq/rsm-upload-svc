package com.edso.resume.file.controller;

import com.edso.resume.file.domain.rabbitmq.Msg;
import com.edso.resume.file.service.RabbitMqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rabbitmq/v1/")
public class RabbitMqController {

    private final RabbitMqSender rabbitMqSender;

    @Autowired
    public RabbitMqController(RabbitMqSender rabbitMqSender) {
        this.rabbitMqSender = rabbitMqSender;
    }

    @PostMapping(value = "message")
    public String publishMsgDetails(@RequestBody Msg msg) {
        rabbitMqSender.send(msg);
        return "Message has been sent successfully";
    }
}

