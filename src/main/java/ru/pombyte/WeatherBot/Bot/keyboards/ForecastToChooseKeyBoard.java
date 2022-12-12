package ru.pombyte.WeatherBot.Bot.keyboards;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.pombyte.WeatherBot.Bot.handlers.cache.BotStateCache;
import ru.pombyte.WeatherBot.Bot.util.HelpMethods;
import ru.pombyte.WeatherBot.models.ForecastDTO;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Клавиатура для выбора прогноза погоды на конкретный день
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ForecastToChooseKeyBoard {

    final String calendarEmoji = EmojiParser.parseToUnicode("&#128198;");
    BotStateCache botStateCache;

    public InlineKeyboardMarkup setKeyBoardByDays(ForecastDTO forecast, Message message) {

        String chatId = message.getChatId().toString();
        Long timeZone = forecast.getCity().getTimezone();

        List<WeatherDTO> forecastList = forecast.getForecastList();
        List<WeatherDTO> dayList = new ArrayList<>();
        dayList.add(forecastList.get(0));

        String firstDay = HelpMethods.formatDate("dd MMMM - EEEE",Date.from(Instant.ofEpochSecond(forecastList.get(0).getTime() + timeZone)));

        //Цикл для разделения списка прогнозов по дням
        for (int i = 1; i < forecastList.size(); i++) {

            String currentDay = HelpMethods.formatDate("dd MMMM - EEEE",Date.from(Instant.ofEpochSecond(forecastList.get(i).getTime() + timeZone)));

            if (currentDay.equals(firstDay)) {
                dayList.add(forecastList.get(i));

            } else {
                botStateCache.saveDailyForecast(chatId, firstDay, dayList);
                firstDay = currentDay;
                dayList = new ArrayList<>();
                dayList.add(forecastList.get(i));

            }
        }

        botStateCache.saveDailyForecast(chatId, firstDay, dayList);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        Set<String> dayKeySet = new TreeSet<>(botStateCache.getForecastSet(chatId).stream().map(n -> n.getDay()).collect(Collectors.toSet()));

        for (String day : dayKeySet) {

            var localeButton = new InlineKeyboardButton();
            localeButton.setText(calendarEmoji + " " + day);
            localeButton.setCallbackData(day);

            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            rowLine.add(localeButton);
            rowsInLine.add(rowLine);

        }

        markup.setKeyboard(rowsInLine);

        return markup;

    }
}
