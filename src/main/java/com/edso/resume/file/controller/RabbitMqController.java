package com.edso.resume.file.controller;

import com.edso.resume.file.domain.rabbitmq.Msg;
import com.edso.resume.file.service.RabbitMqSender;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rabbitmq/v1/")
public class RabbitMqController {

    private final RabbitMqSender rabbitMqSender;

    @Autowired
    public RabbitMqController(RabbitMqSender rabbitMqSender) {
        this.rabbitMqSender = rabbitMqSender;
    }

    @PostMapping(value = "message")
    public BaseResponse publishMsgDetails(@RequestBody Msg msg) {
        BaseResponse baseResponse = new BaseResponse();
        rabbitMqSender.send(msg);
        baseResponse.setSuccess("Message has been sent successfully");
        return baseResponse;
    }
}

