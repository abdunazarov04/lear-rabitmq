package uz.javachi.learnrabbitmqbot.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.javachi.learnrabbitmqbot.entity.Order;
import uz.javachi.learnrabbitmqbot.utils.Executor;

import java.util.Optional;

import static uz.javachi.learnrabbitmqbot.utils.StringUtils.extractFirstNumbers;

@Service
public class OrderConsumer {

    private final Executor executor;
    private final OrderService orderService;

    public OrderConsumer(Executor executor, OrderService orderService) {
        this.executor = executor;
        this.orderService = orderService;
    }

    @RabbitListener(queues = "orderQueue")
    public void processOrder(String order) {
        System.out.println("Buyurtma qabul qilindi: " + order);
        sendNotificationToUser(order);
    }

    private void sendNotificationToUser(String order) {
        Optional<Order> orderEntity = orderService.findOrderAndReturnChatId(
                Long.parseLong(extractFirstNumbers(order))
        );
        orderEntity.ifPresent(value -> executor.execute(
                SendMessage.builder()
                        .chatId(value.getChatId())
                        .text(order)
                        .build()
        ));
        System.out.println("Foydalanuvchiga xabar yuborildi: " + order);
    }
}

