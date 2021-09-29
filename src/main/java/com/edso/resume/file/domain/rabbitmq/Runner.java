//package com.edso.resume.file.domain.rabbitmq;
//
//import com.edso.resume.file.domain.rabbitmq.receiver.FirstReceiver;
//import com.edso.resume.file.domain.rabbitmq.receiver.SecondReceiver;
//import com.edso.resume.file.domain.rabbitmq.receiver.ThirdReceiver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.stereotype.Component;
//
//import static com.edso.resume.file.domain.rabbitmq.RabbitApp.EXCHANGE_NAME;
//
//@Component
//public class Runner implements CommandLineRunner {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final RabbitTemplate rabbitTemplate;
//
//    private final FirstReceiver firstReceiver;
//
//    private final SecondReceiver secondReceiver;
//
//    private final ThirdReceiver thirdReceiver;
//
//    private final ConfigurableApplicationContext context;
//
//
//    public Runner(FirstReceiver firstReceiver,SecondReceiver secondReceiver, ThirdReceiver thirdReceiver,
//                  RabbitTemplate rabbitTemplate,
//                  ConfigurableApplicationContext context) {
//        this.firstReceiver = firstReceiver;
//        this.secondReceiver=secondReceiver;
//        this.thirdReceiver = thirdReceiver;
//        this.rabbitTemplate = rabbitTemplate;
//        this.context = context;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        logger.info("Sending message...");
//
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,FirstReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 1!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,SecondReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 2!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ThirdReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 3!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,FirstReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 4!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,SecondReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 5!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ThirdReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 6!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ThirdReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 7!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,FirstReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 8!");
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,SecondReceiver.QUEUE_ROUTINGKEY, "Hello from RabbitMQ Sent 9!");
//
//        context.close();
//    }
//
//    public FirstReceiver getFirstReceiver() {
//        return firstReceiver;
//    }
//
//    public SecondReceiver getSecondReceiver() {
//        return secondReceiver;
//    }
//
//    public ThirdReceiver getThirdReceiver() {
//        return thirdReceiver;
//    }
//}