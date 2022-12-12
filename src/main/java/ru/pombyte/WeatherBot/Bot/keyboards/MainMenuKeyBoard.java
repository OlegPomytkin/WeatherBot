package ru.pombyte.WeatherBot.Bot.keyboards;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура главное меню
 **/
@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainMenuKeyBoard {

    final String helpEmojiButton = EmojiParser.parseToUnicode(":information_source: Помощь");
    final String chooseCityEmojiButton = EmojiParser.parseToUnicode("&#127961; Изменить город");
    final String weatherEmojiButton = EmojiParser.parseToUnicode(":partly_sunny: Погода сейчас");
    final String forecastEmojiButton = EmojiParser.parseToUnicode("&#128198; Прогноз погоды");
    final String notifyButton = "/notify";
    final String altChooseCityButton = "/city";
    final String altWeatherButton = "/weather";
    final String altForecastButton = "/forecast";

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(weatherEmojiButton));
        row1.add(new KeyboardButton(forecastEmojiButton));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(chooseCityEmojiButton));
        row2.add(new KeyboardButton(helpEmojiButton));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;

    }

}
