package uz.javachi.learnrabbitmqbot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KeyboardMarkup {


    public static KeyboardButton button(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }

    public static KeyboardButton button(String text, boolean contact) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        button.setRequestContact(contact);
        return button;
    }

    public static List<KeyboardRow> row(KeyboardButton... buttons) {
        KeyboardRow row = new KeyboardRow();
        row.addAll(Arrays.asList(buttons));
        return Collections.singletonList(row);
    }

    @SafeVarargs
    public static ReplyKeyboardMarkup keyboardMarkup(List<KeyboardRow>... rows) {
        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setOneTimeKeyboard(true);
        replyMarkup.setResizeKeyboard(true);
        List<KeyboardRow> allRows = new LinkedList<>();

        for (List<KeyboardRow> row : rows) {
            allRows.addAll(row);
        }
        replyMarkup.setKeyboard(allRows);
        return replyMarkup;
    }


}
