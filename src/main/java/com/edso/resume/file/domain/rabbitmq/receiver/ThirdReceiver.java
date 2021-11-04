package com.edso.resume.file.domain.rabbitmq.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static com.edso.resume.file.domain.rabbitmq.RabbitApp.EXCHANGE_NAME;

@Component
public class ThirdReceiver {

    private final static String QUEUE_NAME = "spring-boot3";
    public final static String QUEUE_ROUTINGKEY = "routingKey3";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer counter = 0;

    private final CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = EXCHANGE_NAME),
            key = QUEUE_ROUTINGKEY))
    public void receiveMessage(String message) {
        logger.info("From receiver 3: Received <{}>", message);
        counter++;
    }

    public Integer getCounter() {
        return counter;
    }

    public void initCounter() {
        this.counter = 0;
    }
}
