package uz.javachi.learnrabbitmqbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.javachi.learnrabbitmqbot.entity.Order;
import uz.javachi.learnrabbitmqbot.map.OrderMapTemporary;
import uz.javachi.learnrabbitmqbot.repository.OrderRepository;
import uz.javachi.learnrabbitmqbot.utils.Executor;
import uz.javachi.learnrabbitmqbot.utils.InlineKeyboards;
import uz.javachi.learnrabbitmqbot.utils.KeyboardMarkup;

import java.util.Optional;

@Service
public class OrderService {
    private final String mainImgId;
    private final Executor executor;
    private final OrderRepository orderRepository;
    private final OrderMapTemporary order;

    private final OrderPublisher orderPublisher;


    @Value("${telegram.order.chat-id}")
    private Long orderGroupId;

    @Value("${spring.rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    public OrderService(
            @Value("${telegram.bot.main.image.id}") String mainImgId,
            Executor executor,
            OrderRepository orderRepository,
            OrderMapTemporary order,
            OrderPublisher orderPublisher
    ) {
        this.mainImgId = mainImgId;
        this.executor = executor;
        this.orderRepository = orderRepository;
        this.order = order;
        this.orderPublisher = orderPublisher;
    }


    public void getOrder(Long chatId) {

        executor.execute(
                SendPhoto.builder()
                        .chatId(chatId)
                        .caption("""
                                <b>Assalomu Alaykum BOT ga Xush kelibsiz,
                                Iltimos menudan taom tanlang!!!</b>
                                """)
                        .parseMode("HTML")
                        .photo(new InputFile(mainImgId))
                        .replyMarkup(
                                InlineKeyboards.keyboardMarkup(
                                        InlineKeyboards.collection(
                                                InlineKeyboards.row(
                                                        InlineKeyboards.button("Lavash", "/order/Lavash"),
                                                        InlineKeyboards.button("Burger", "/order/Burger")
                                                ),
                                                InlineKeyboards.row(
                                                        InlineKeyboards.button("Shaurma", "/order/Shaurma"),
                                                        InlineKeyboards.button("Pizza", "/order/Pizza")
                                                )
                                        )
                                )
                        )
                        .build()
        );

    }

    public void saveOrder(Long chatId, String text) {
        Order order1 = order.getOrder(chatId);
        order1.setOptionalMessage(order1.getOptionalMessage() + " \n\nMessage: " + text);
        orderRepository.save(order1);
        order.deletedOrder(chatId);

        executor.execute(
                SendMessage.builder()
                        .chatId(orderGroupId)
                        .parseMode("HTML")
                        .text(order1.getOptionalMessage())
                        .replyMarkup(
                                InlineKeyboards.keyboardMarkup(
                                        InlineKeyboards.collection(
                                                InlineKeyboards.row(InlineKeyboards.button("Qabul qilish", "/chef/order/accept/" + order1.getId())),
                                                InlineKeyboards.row(InlineKeyboards.button("Rad etish", "/chef/order/not-accept/" + order1.getId()))
                                        )
                                )
                        )
                        .build()
        );
        executor.execute(
                SendMessage.builder()
                        .chatId(chatId)
                        .parseMode("HTML")
                        .text("<b>\uD83D\uDDF3 Buyutma raqami: %d</b>\n<i>Buyurtmangiz qabul qilindi\nIltimos buyurtmangiz tasdiqlanishini kuting!</i>".formatted(order1.getId()))
                        .replyMarkup(KeyboardMarkup.keyboardMarkup(
                                KeyboardMarkup.row(
                                        KeyboardMarkup.button("Buyurtma berish\uD83D\uDCDD")
                                )
                        ))
                        .build()
        );
    }

    public void handleOrder(String order) {
        orderPublisher.publishOrder(exchangeName, routingKey, order);
    }

    public void chefAndOrder(String text) {
        String[] message = text.split("/");
        String command = message[3];
        Integer orderId = Integer.parseInt(message[4]);
        String orderMessage = """
                %d raqamli buyurtma qabul qilindi.
                30 daqiqa ichida yetkazib beruvchi siz bilan bog'lanadi!
                """;
        if (this.orderRepository.findOrderById(orderId).isPresent()) {
            switch (command) {
                case "accept" -> handleOrder(orderMessage.formatted(orderId));
                case "not-accept" ->
                        handleOrder("%d raqamli buyurtma ma'lum bir sabablarga ko'ra qabul qilinmadi!".formatted(orderId));
            }
        } else {
            executor.execute(
                    SendMessage.builder()
                            .chatId(orderGroupId)
                            .parseMode("HTML")
                            .text("%d raqamli buyurtma topilmadi!".formatted(orderId))
                            .build()
            );
        }

    }

    public Optional<Order> findOrderAndReturnChatId(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
