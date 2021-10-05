package com.edso.resume.file.consumer;

import com.edso.resume.file.domain.entities.Event;
import com.edso.resume.file.service.CVService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private final String EVENT_CREATE = "create";
    private final String EVENT_UPDATE = "update";
    private final String EVENT_DELETE = "delete";

    private final CVService cvService;

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public Consumer(CVService cvService) {
        this.cvService = cvService;
    }

    @SneakyThrows
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeEvent(Event event) {

        logger.info("Event from queue " + event);
        switch (event.getType()) {
            case EVENT_CREATE:
                cvService.create(event);
                break;
            case EVENT_UPDATE:
                cvService.update(event);
                break;
            case EVENT_DELETE:
                cvService.delete(event.getProfile().getId());
                break;
            default:
                logger.info("Invalid type");
        }

    }
}
