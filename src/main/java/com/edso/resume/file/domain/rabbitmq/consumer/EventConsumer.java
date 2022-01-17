package com.edso.resume.file.domain.rabbitmq.consumer;

import com.edso.resume.file.domain.rabbitmq.event.ProfileEvent;
import com.edso.resume.file.service.CVService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.edso.resume.lib.common.RabbitMQConfig;

@Component
public class EventConsumer {

    private final CVService cvService;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    public EventConsumer(CVService cvService) {
        this.cvService = cvService;
    }

    @SneakyThrows
    @RabbitListener(queues = "${spring.rabbitmq.profile.queue}")
    public void consumeEvent(ProfileEvent profileEvent) {

        logger.info("Event from queue " + profileEvent);
        switch (profileEvent.getType()) {
            case RabbitMQConfig.CREATE:
                cvService.create(profileEvent);
                break;
            case RabbitMQConfig.UPDATE:
            case RabbitMQConfig.UPDATE_DETAIL:
                cvService.update(profileEvent);
                break;
            case RabbitMQConfig.DELETE:
                cvService.delete(profileEvent.getProfile().getId());
                break;
            case RabbitMQConfig.UPDATE_STATUS:
                cvService.updateStatus(profileEvent);
                break;
            case RabbitMQConfig.UPDATE_IMAGE:
                cvService.updateImages(profileEvent);
                break;
            case RabbitMQConfig.DELETE_IMAGE:
                cvService.deleteImages(profileEvent);
                break;
            default:
                logger.info("Invalid type");
        }

    }
}
