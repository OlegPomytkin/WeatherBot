package ru.pombyte.WeatherBot.Bot.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pombyte.WeatherBot.Bot.WeatherBot;
import ru.pombyte.WeatherBot.Bot.handlers.cache.BotStateCache;
import ru.pombyte.WeatherBot.Bot.util.HelpMethods;
import ru.pombyte.WeatherBot.models.ForecastDTO;
import ru.pombyte.WeatherBot.models.Notification;
import ru.pombyte.WeatherBot.models.User;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import ru.pombyte.WeatherBot.services.ForecastService;
import ru.pombyte.WeatherBot.services.NotificationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleTasksHandler {

    NotificationService notificationService;
    ForecastService forecastService;
    CallBackQueryHandler callBackQueryHandler;
    BotStateCache botStateCache;

    public void handleTask (WeatherBot bot){

        DateFormat df = new SimpleDateFormat("H", new Locale("ru"));
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        List<Notification> taskList = notificationService.getAllNotifications();
        Long instantNow = Instant.now().getEpochSecond();

        for (Notification notification: taskList){

            User user = notification.getUser();
            String chatId = String.valueOf(user.getChatId());
            String time = notification.getTime();
            Long timezone = notification.getTimezone();
            Date actualTime = Date.from(Instant.ofEpochSecond(instantNow + timezone));
            String actualHour = df.format(actualTime);

            System.out.println(chatId + ": " + timezone + "; " + actualHour);
            System.out.println(chatId + ": " + time);

            if (actualHour.equals(time)) {

                ForecastDTO forecastDTOResponse = forecastService.requestForecastData(user.getLocale().getLat(), user.getLocale().getLon());
                botStateCache.saveForecast(chatId, forecastDTOResponse);
                List<WeatherDTO> forecastList = forecastDTOResponse.getForecastList();
                List<WeatherDTO> dayList = new ArrayList<>();
                dayList.add(forecastList.get(0));

                String firstDay = HelpMethods.formatDate("dd MMMM - EEEE",Date.from(Instant.ofEpochSecond(forecastList.get(0).getTime() + timezone)));

                //Цикл для разделения списка прогнозов для текущего дня
                for (int i = 1; i < forecastList.size(); i++) {

                    String currentDay = HelpMethods.formatDate("dd MMMM - EEEE",Date.from(Instant.ofEpochSecond(forecastList.get(i).getTime() + timezone)));

                    if (currentDay.equals(firstDay)) {
                        dayList.add(forecastList.get(i));

                    } else {
                        botStateCache.saveDailyForecast(chatId, firstDay, dayList);
                        break;
                    }
                }

                botStateCache.saveDailyForecast(chatId, firstDay, dayList);
                SendMessage sendMessage = callBackQueryHandler.getForecastMessage(chatId, dayList, firstDay);

                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
