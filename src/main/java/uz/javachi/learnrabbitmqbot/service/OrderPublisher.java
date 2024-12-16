package uz.javachi.learnrabbitmqbot.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrder(String exchange, String routingKey, String order) {
        rabbitTemplate.convertAndSend(exchange, routingKey, order);
        System.out.println("Buyurtma queuga yuborildi!");
    }
}