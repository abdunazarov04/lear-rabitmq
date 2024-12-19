package uz.javachi.learnrabbitmqbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.javachi.learnrabbitmqbot.config.BotConfig;

@SpringBootApplication
public class LearnRabbitmqBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnRabbitmqBotApplication.class, args);
    }

    @Component
    static class RegisterBot {

        private final BotConfig botConfig;

        public RegisterBot(BotConfig botConfig) {
            this.botConfig = botConfig;
        }

        @EventListener({ContextRefreshedEvent.class})
        public void init() {
            try {
                TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
                api.registerBot(botConfig);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    * TODO:
    *   RabbitMQ ->
    *       Config ->
    *           Telegram bot ->
    *                     Order ->
    *                        Chef ->
    *                           RabbitMQ - Publish ->
    *                                   RabbitMQ - Consume ->
    *                                       Notification to user;
    * */

}
