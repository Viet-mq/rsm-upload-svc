package com.edso.resume.file.publisher;

import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.entities.Event;
import com.edso.resume.file.domain.rabbitmq.Msg;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @PostMapping("/publish")
    public BaseResponse addEvent(@RequestParam String type, @RequestBody(required = false) CV cv){
        cv.setId(UUID.randomUUID().toString());
        Event event = new Event(type, cv);
        rabbitTemplate.convertAndSend(exchange, routingkey, event);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess("Published");
        return baseResponse;
    }
}