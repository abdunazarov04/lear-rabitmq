package uz.javachi.learnrabbitmqbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.reactions.SetMessageReaction;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionTypeEmoji;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.javachi.learnrabbitmqbot.entity.Order;
import uz.javachi.learnrabbitmqbot.map.OrderMapTemporary;
import uz.javachi.learnrabbitmqbot.service.OrderService;
import uz.javachi.learnrabbitmqbot.utils.Executor;

import java.util.Collections;
import java.util.List;

@Component
public class BotConfig extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    //TODO: 1
    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    private final OrderService orderService;
    private final OrderMapTemporary order;
    private final Executor executor;

    public BotConfig(
            @Lazy OrderService orderService,
            OrderMapTemporary order,
            Executor executor
    ) {
        this.orderService = orderService;
        this.executor = executor;
        this.order = order;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            Integer messageId = update.getMessage().getMessageId();

            addReaction(chatId, messageId, "\uD83D\uDC40");
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String text = callbackQuery.getData();
            Long chatId = callbackQuery.getFrom().getId();

            String foodName = text.split("/")[2];
            if (text.startsWith("/order")) {
                order.setOrder(chatId, new Order(
                        chatId, "<b>" + foodName + "</b> "
                ));
                executor.execute(
                        EditMessageCaption.builder()
                                .chatId(chatId)
                                .caption("<i>Buyurma uchun qandaydir izoh qoldirishni istaysizmi ?</i>")
                                .parseMode("HTML")
                                .messageId(callbackQuery.getMessage().getMessageId())
                                .build()
                );
            }else if (text.startsWith("/chef/order")) {
                orderService.chefAndOrder(text);
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            Long chatId = message.getChatId();

            if (text.equals("/start") || text.equals("Buyurtma berish\uD83D\uDCDD")) {
                orderService.getOrder(chatId);
            } else if (text.equals("/help")) {
//                botService.help(chatId);
            } else if (order.orderStatus(chatId)) {
                orderService.saveOrder(chatId, text);
            }
        }
    }


    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void addReaction(Long chatId, Integer messageId, String emoji) {
        SetMessageReaction reaction = new SetMessageReaction();
        reaction.setChatId(chatId);
        reaction.setMessageId(messageId);
        reaction.setReactionTypes(List.of(
                ReactionTypeEmoji.builder()
                        .emoji(emoji)
                        .build()
        ));

        try {
            execute(reaction);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

