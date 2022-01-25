package com.edso.resume.file.domain.rabbitmq.publisher;

import com.edso.resume.file.domain.entities.Profile;
import com.edso.resume.file.domain.rabbitmq.event.ProfileEvent;
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

    @Value("${spring.rabbitmq.profile.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.profile.routingkey}")
    private String routingkey;

    @PostMapping("/publish/{type}")
    public BaseResponse addEvent(@PathVariable("type") String type,
                                 @RequestBody(required = false) Profile profile) {
        ProfileEvent profileEvent = new ProfileEvent(type, profile);
        rabbitTemplate.convertAndSend(exchange, routingkey, profileEvent);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess("Published");
        return baseResponse;
    }
}
