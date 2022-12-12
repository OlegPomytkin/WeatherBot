package ru.pombyte.WeatherBot.Bot.keyboards;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetTimeNotifyKeyboard {

    public InlineKeyboardMarkup setKeyBoardForTime () {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        for (int i = 0; i < 24; i++) {

            var localeButton = new InlineKeyboardButton();
            localeButton.setText(String.format("%02d:00", i));
            localeButton.setCallbackData(String.valueOf(i));

            if(i % 6 == 0) {
                rowsInLine.add(rowLine);
                rowLine = new ArrayList<>();
            }
            rowLine.add(localeButton);

        }

        rowsInLine.add(rowLine);

        var rejectButton = new InlineKeyboardButton();
        rejectButton.setText("Отключить авторассылку");
        rejectButton.setCallbackData(String.valueOf(-1));
        rowLine = new ArrayList<>();
        rowLine.add(rejectButton);

        rowsInLine.add(rowLine);

        markup.setKeyboard(rowsInLine);

        return markup;

    }
}
