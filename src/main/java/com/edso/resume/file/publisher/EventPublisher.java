package com.edso.resume.file.publisher;

import com.edso.resume.file.domain.entities.Image;
import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.entities.Event;
import com.edso.resume.lib.response.BaseResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @PostMapping("/publish/{type}")
    public BaseResponse addEvent(@PathVariable("type") String type,
                                 @RequestBody(required = false) Profile profile){
        Event event = new Event(type, profile);
        rabbitTemplate.convertAndSend(exchange, routingkey, event);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess("Published");
        return baseResponse;
    }
}
