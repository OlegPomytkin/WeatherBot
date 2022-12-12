package ru.pombyte.WeatherBot.Bot.handlers;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.pombyte.WeatherBot.Bot.States;
import ru.pombyte.WeatherBot.Bot.handlers.cache.BotStateCache;
import ru.pombyte.WeatherBot.Bot.util.HelpMethods;
import ru.pombyte.WeatherBot.models.ForecastDTO;
import ru.pombyte.WeatherBot.models.Locale;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import ru.pombyte.WeatherBot.models.User;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import ru.pombyte.WeatherBot.models.Weather.Wind;
import ru.pombyte.WeatherBot.services.LocaleService;
import ru.pombyte.WeatherBot.services.NotificationService;
import ru.pombyte.WeatherBot.services.UserService;
import ru.pombyte.WeatherBot.services.WeatherService;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Обработчик динамических кнопок Callback
 **/
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CallBackQueryHandler {

    final String errorMessage = EmojiParser.parseToUnicode("Произошла ошибка :scream: пожалуйста, попробуйте ещё раз - нажмите на нужный пункт меню.");

    BotStateCache botStateCache;
    UserService userService;
    LocaleService localeService;
    WeatherService weatherService;
    NotificationService notificationService;
    ModelMapper modelMapper;

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {

        Message message = buttonQuery.getMessage();
        String chatId = message.getChatId().toString();
        String data = buttonQuery.getData();
        States botState = botStateCache.getBotState(chatId);

/**Обработка списка выбора города**/

        if (botState.equals(States.CHOOSE_CITY)) {

            LocaleDTO chosenCity = botStateCache.getFoundCities(chatId).get(Integer.parseInt(data));
            HelpMethods.changeBotState(botStateCache, chatId, States.NEW);

            if (userService.findOne(message) == null)

                userService.save(message);

            else if (userService.findOne(message).getLocale() != null)

                userService.deleteCity(message);

            userService.updateCity(message, convertToLocale(chosenCity));

            return getCityChosenMessage(message, chosenCity);

        }

/**Обработка списка прогноза погоды**/

        if (botState.equals(States.FORECAST_VIEW)) {

            List<WeatherDTO> dailyForecastList = botStateCache.getForecastByDaysList(chatId,data);

            if (dailyForecastList != null)
                return getForecastMessage(chatId, dailyForecastList, data);

            else return getNoDataMessage(message);

        }

/**Обработка списка установить время**/

        if (botState.equals(States.SET_NOTIFY)) {

            if(!data.equals("-1")) {

                User settingUser = userService.findOne(message);
                WeatherDTO requestForTimeZone = weatherService.requestWeatherData(settingUser.getLocale().getLat(), settingUser.getLocale().getLon());
                Long offset = requestForTimeZone.getTimezone();

                notificationService.save(message, offset, data);
                HelpMethods.changeBotState(botStateCache, chatId, States.NEW);

                return getTimeSetMessage(message, data);

            }
            else {

                notificationService.resetNotification(message);
                HelpMethods.changeBotState(botStateCache, chatId, States.NEW);

                return getTimeRemovedMessage(message);
            }

        }

        return new SendMessage(chatId, errorMessage);

    }

    private SendMessage getNoDataMessage (Message message){

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Данные устарели, пожалуйста повторите команду еще раз");
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }


    private SendMessage getCityChosenMessage(Message message, LocaleDTO city) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), EmojiParser.parseToUnicode("&#127961; Выбранный Вами город: " + city.getName()));
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getTimeSetMessage(Message message, String time) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), EmojiParser.parseToUnicode("&#x23F0; Время уведомления установлено на: " + String.format("*%02d:00*", Integer.parseInt(time))));
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getTimeRemovedMessage (Message message) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), EmojiParser.parseToUnicode("&#x23F0; *Уведомления отключены!*"));
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    /**
     * Обработчик представления для прогноза погоды из кэша передается лист для конкретного дня погоды, само разделение прогноза по дням происходит при создании клавиатуры ForecastToChooseKeyboard
     **/
    public SendMessage getForecastMessage(String chatId, List<WeatherDTO> foreCastList, String data) {

        StringBuilder weatherUnit = new StringBuilder();
        ForecastDTO userForecast = botStateCache.getForecast(chatId);
        Long timeShift = userForecast.getCity().getTimezone();
        Date sunRise = Date.from(Instant.ofEpochSecond(userForecast.getCity().getSunrise() + timeShift));
        Date sunSet = Date.from(Instant.ofEpochSecond(userForecast.getCity().getSunset() + timeShift));
        String datePattern = "*Время: HH:mm*";

        weatherUnit.append("Погода в *" + userForecast.getCity().getName() + "* на *" + data + "*:");
        weatherUnit.append(EmojiParser.parseToUnicode("\n\n&#127749; *Восход солнца*: ") + HelpMethods.formatDate(datePattern, sunRise));
        weatherUnit.append(EmojiParser.parseToUnicode("\n&#127751; *Заход солнца*: ") + HelpMethods.formatDate(datePattern, sunSet));

        //Счетчик для пропуска данных через раз
        int i = 0;

        for (WeatherDTO weatherDTO : foreCastList) {
            i++;
            if (i % 2 == 0)
                continue;

            Date time = Date.from(Instant.ofEpochSecond(weatherDTO.getTime() + timeShift));
            weatherUnit.append("\n\n\n" + HelpMethods.formatDate(datePattern, time));

            HelpMethods.fillTemperatureInfo(weatherUnit, weatherDTO);

            Wind wind = weatherDTO.getWind();
            String direction = HelpMethods.parseWind(wind);

            weatherUnit.append(EmojiParser.parseToUnicode("\n\n&#128168; *Ветер*: ") + direction);
            HelpMethods.fillFinalPartInfo(weatherUnit, weatherDTO);


        }

        SendMessage sendMessage = new SendMessage(chatId, weatherUnit.toString());
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    /**
     * Блок вспомогательных методов
     **/
    private Locale convertToLocale(LocaleDTO localeDTO) {

        return modelMapper.map(localeDTO, Locale.class);

    }

}
