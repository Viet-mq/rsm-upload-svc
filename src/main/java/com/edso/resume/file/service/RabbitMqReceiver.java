//package com.edso.resume.file.service;
//
//import com.edso.resume.file.domain.rabbitmq.Msg;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
//import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
//import org.springframework.stereotype.Component;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Component
//public class RabbitMqReceiver implements RabbitListenerConfigurer {
//
//    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
//
//    @RabbitListener(queues = "${spring.rabbitmq.queue}")
//    public void receivedMessage(Msg msg) {
//
//        logger.info("Msg Details Received is.. " + msg);
//    }
//
//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
//
//    }
//}