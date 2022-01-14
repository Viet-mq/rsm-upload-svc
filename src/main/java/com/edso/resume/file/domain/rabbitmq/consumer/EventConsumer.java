package com.edso.resume.file.domain.rabbitmq.consumer;

import com.edso.resume.file.domain.entities.Event;
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
    public void consumeEvent(Event event) {

        logger.info("Event from queue " + event);
        switch (event.getType()) {
            case RabbitMQConfig.CREATE:
                cvService.create(event);
                break;
            case RabbitMQConfig.UPDATE:
            case RabbitMQConfig.UPDATE_DETAIL:
                cvService.update(event);
                break;
            case RabbitMQConfig.DELETE:
                cvService.delete(event.getProfile().getId());
                break;
            case RabbitMQConfig.UPDATE_STATUS:
                cvService.updateStatus(event);
                break;
            case RabbitMQConfig.UPDATE_IMAGE:
                cvService.updateImages(event);
                break;
            case RabbitMQConfig.DELETE_IMAGE:
                cvService.deleteImages(event);
                break;
            default:
                logger.info("Invalid type");
        }

    }
}
