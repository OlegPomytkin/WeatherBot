package ru.pombyte.WeatherBot.Bot.keyboards;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Динамическая клавиатура для выбора из списка городов
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CitiesToChoseKeyBoard {
    final String cityEmoji = EmojiParser.parseToUnicode("&#127961;");

    public InlineKeyboardMarkup setKeyBoardForCities(List<LocaleDTO> list) {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {

            LocaleDTO localeToChoose = list.get(i);

            var localeButton = new InlineKeyboardButton();
            localeButton.setText(cityEmoji + " " + localeToChoose.getCountry() + ": Регион: " + localeToChoose.getState() + "; Город: " + localeToChoose.getName());
            localeButton.setCallbackData(String.valueOf(i));

            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            rowLine.add(localeButton);
            rowsInLine.add(rowLine);

        }

        markup.setKeyboard(rowsInLine);

        return markup;

    }
}
