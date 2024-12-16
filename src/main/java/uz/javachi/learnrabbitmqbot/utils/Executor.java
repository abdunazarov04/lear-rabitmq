package uz.javachi.learnrabbitmqbot.utils;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Executor {

    private final AbsSender bot;

    Executor(@Lazy AbsSender bot) {
        this.bot = bot;
    }


    public Message execute(Object object) {
        try {
            switch (object) {
                case SendMessage sendMessage -> {
                    return bot.execute(sendMessage);
                }
                case SendPhoto sendPhoto -> {
                    return bot.execute(sendPhoto);
                }
                case SendDocument sendDocument -> {
                    return bot.execute(sendDocument);
                }
                case SendVideo sendVideo -> {
                    return bot.execute(sendVideo);
                }
                case SendAudio sendAudio -> {
                    return bot.execute(sendAudio);
                }
                case SendVideoNote sendVideoNote -> {
                    return bot.execute(sendVideoNote);
                }
                case SendVoice sendVoice -> {
                    return bot.execute(sendVoice);
                }
                case SendLocation sendLocation -> {
                    return bot.execute(sendLocation);
                }
                case SendContact sendContact -> {
                    return bot.execute(sendContact);
                }
                case SendPoll sendPoll -> {
                    return bot.execute(sendPoll);
                }
                case EditMessageText editMessageText -> bot.execute(editMessageText);
                case EditMessageCaption editMessageCaption -> bot.execute(editMessageCaption);
                case EditMessageReplyMarkup editMessageReplyMarkup -> bot.execute(editMessageReplyMarkup);
                case DeleteMessage deleteMessage -> bot.execute(deleteMessage);
                case AnswerCallbackQuery answerCallbackQuery -> bot.execute(answerCallbackQuery);
                case AnswerPreCheckoutQuery answerPreCheckoutQuery -> bot.execute(answerPreCheckoutQuery);
                case null, default -> {
                    assert object != null;
                    throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
