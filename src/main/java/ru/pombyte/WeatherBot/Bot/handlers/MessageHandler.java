package ru.pombyte.WeatherBot.Bot.handlers;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.pombyte.WeatherBot.Bot.States;
import ru.pombyte.WeatherBot.Bot.handlers.cache.BotStateCache;
import ru.pombyte.WeatherBot.Bot.keyboards.CitiesToChoseKeyBoard;
import ru.pombyte.WeatherBot.Bot.keyboards.ForecastToChooseKeyBoard;
import ru.pombyte.WeatherBot.Bot.keyboards.MainMenuKeyBoard;
import ru.pombyte.WeatherBot.Bot.keyboards.SetTimeNotifyKeyboard;
import ru.pombyte.WeatherBot.Bot.util.HelpMethods;
import ru.pombyte.WeatherBot.models.ForecastDTO;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import ru.pombyte.WeatherBot.models.User;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import ru.pombyte.WeatherBot.models.Weather.Wind;
import ru.pombyte.WeatherBot.services.ForecastService;
import ru.pombyte.WeatherBot.services.LocaleService;
import ru.pombyte.WeatherBot.services.UserService;
import ru.pombyte.WeatherBot.services.WeatherService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Обработчик сообщений
 **/
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {
    final String failCommand = "Не удалось распознать Вашу команду, пожалуйста, воспользуйтесь кнопками в нижнем меню.";
    BotStateCache botStateCache;
    MainMenuKeyBoard mainMenuKeyBoard;
    CitiesToChoseKeyBoard citiesToChoseKeyBoard;
    ForecastToChooseKeyBoard forecastToChooseKeyBoard;
    SetTimeNotifyKeyboard setTimeNotifyKeyboard;
    UserService userService;
    LocaleService localeService;
    WeatherService weatherService;
    ForecastService forecastService;

    public BotApiMethod<?> answerMessage(Message message) {

        String chatId = message.getChatId().toString();
        String inputText = message.getText();
        States botState = botStateCache.getBotState(chatId);

/** Блок обработки для статуса NEW (Не выбрано ни одного действия)**/

        if (inputText == null) {

            throw new IllegalArgumentException();

        } else if (inputText.equals("/start")) {

            return getStartMessage(message);

        } else if (inputText.equals(mainMenuKeyBoard.getChooseCityEmojiButton()) || inputText.equals(mainMenuKeyBoard.getAltChooseCityButton())) {

            HelpMethods.changeBotState(botStateCache, chatId, States.REGISTER_CITY);

            return getChooseCityMessage(message);

        } else if (inputText.equals(mainMenuKeyBoard.getHelpEmojiButton())) {

            return getHelpMessage(message);

        }

/** Блок обработки для механизма просмотра прогноза (Сатус FORECAST_VIEW)**/

        if (inputText.equals(mainMenuKeyBoard.getForecastEmojiButton()) || inputText.equals(mainMenuKeyBoard.getAltForecastButton())) {

            User user = userService.findOne(message);

            if (user == null || user.getLocale() == null) {

                HelpMethods.changeBotState(botStateCache, chatId, States.REGISTER_CITY);

                return getChooseCityMessage(message);

            } else {

                botStateCache.resetForecastCache(chatId);
                ForecastDTO forecastDTOResponse = forecastService.requestForecastData(user.getLocale().getLat(), user.getLocale().getLon());
                HelpMethods.changeBotState(botStateCache, chatId, States.FORECAST_VIEW);
                botStateCache.saveForecast(chatId, forecastDTOResponse);

                return getForecastToChooseMessage(message, forecastDTOResponse);
            }

        }

/** Блок обработки для механизма узнать погоду сейчас**/

        if (inputText.equals(mainMenuKeyBoard.getWeatherEmojiButton()) || inputText.equals(mainMenuKeyBoard.getAltWeatherButton())) {

            User user = userService.findOne(message);

            if (user == null || user.getLocale() == null) {

                HelpMethods.changeBotState(botStateCache, message.getChatId().toString(), States.REGISTER_CITY);

                return getChooseCityMessage(message);

            } else {

                WeatherDTO weatherResponse = weatherService.requestWeatherData(user.getLocale().getLat(), user.getLocale().getLon());
                HelpMethods.changeBotState(botStateCache, message.getChatId().toString(), States.NEW);

                return weatherResponseMessage(message, weatherResponse);
            }

        }

/** Блок обработки настройка уведомлений (Статус SET_NOTIFY)**/

        if (inputText.equals(mainMenuKeyBoard.getNotifyButton())) {

            User user = userService.findOne(message);

            if (user == null || user.getLocale() == null) {

                HelpMethods.changeBotState(botStateCache, message.getChatId().toString(), States.REGISTER_CITY);

                return getChooseCityMessage(message);

            } else {

                HelpMethods.changeBotState(botStateCache, message.getChatId().toString(), States.SET_NOTIFY);

                return getChooseTimeMessage(message);
            }

        }

/** Блок обработки для механизма выбора города (Сатусы REGISTER_CITY и CHOOSE_CITY)**/

        if (botState == States.REGISTER_CITY) {

            List<LocaleDTO> localeList = localeService.requestLocale(inputText);

            if (localeList.isEmpty()) {

                HelpMethods.changeBotState(botStateCache, chatId, States.NEW);

                return new SendMessage(chatId, EmojiParser.parseToUnicode("К сожалению, мы не нашли ни одного города по Вашему запросу :disappointed:" +
                        "\nПожалуйста, еще раз нажмите на кнопку \"Выбрать Город\""));
            } else {

                HelpMethods.changeBotState(botStateCache, chatId, States.CHOOSE_CITY);
                botStateCache.saveFoundCities(chatId, localeList);

                return getChooseCityFromListMessage(message, localeList);
            }
        } else return new SendMessage(chatId, failCommand);

    }

    private SendMessage getStartMessage(Message message) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), EmojiParser.parseToUnicode("Здравствуйте, "
                + message.getChat().getFirstName() + "!\n\nО том, как пользоваться этим приложением Вы можете узнать, нажав кнопку :information_source: *Помощь*."));
        sendMessage.setReplyMarkup(mainMenuKeyBoard.getMainMenuKeyboard());
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getHelpMessage(Message message) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), EmojiParser.parseToUnicode("Бот может предоставлять информацию о погоде для любого населенного пункта в мире.\n\n" +
                "Вы можете узнать погоду на данный момент времени, нажав кнопку *" + mainMenuKeyBoard.getWeatherEmojiButton() + "*"
                + "\n\nТакже бот предоставляет информацию о погоде на 5 дней, при нажатии на кнопку *" + mainMenuKeyBoard.getForecastEmojiButton() + "*"
                + "\n\nЕсли город не выбран - бот сначала предложит вам это сделать. Вы также можете изменить город в любой момент, нажав кнопку *" + mainMenuKeyBoard.getChooseCityEmojiButton() + "*"
                + "\n\nБот также может отправлять автоматические уведомления о погоде в выбранные вами часы, для настройки или отключения уведомлений воспользуйтесь командой */notify*, находящейся по кнопке *menu*"
                + "\n\nОбращаем внимание, что каждая новая команда должна начинаться с нажатия соответствующей кнопки."));

        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getChooseCityMessage(Message message) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Введите название Вашего города:");
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getChooseTimeMessage(Message message) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Выберите время для автоматической отправки прогноза:");
        sendMessage.setReplyMarkup(setTimeNotifyKeyboard.setKeyBoardForTime());

        return sendMessage;

    }

    private SendMessage getChooseCityFromListMessage(Message message, List<LocaleDTO> list) {

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "Вот, что мы нашли по Вашему запросу, выберите нужный Вам город:");
        sendMessage.setReplyMarkup(citiesToChoseKeyBoard.setKeyBoardForCities(list));

        return sendMessage;

    }

    /**
     * Получаем сущность погоды и заполняем ответ для функции "Узнать погоду сейчас"
     **/
    private SendMessage weatherResponseMessage(Message message, WeatherDTO weatherToSend) {

        Date timeNow = Date.from(Instant.ofEpochSecond(weatherToSend.getTime() + weatherToSend.getTimezone()));
        Date sunrise = Date.from(Instant.ofEpochSecond(weatherToSend.getSys().getSunrise() + weatherToSend.getTimezone()));
        Date sunset = Date.from(Instant.ofEpochSecond(weatherToSend.getSys().getSunset() + weatherToSend.getTimezone()));
        String datePattern = "HH:mm";

        StringBuilder stringBuilder = new StringBuilder("Погода в *" + weatherToSend.getName() + "* время: " + HelpMethods.formatDate(datePattern, timeNow) + "\n");

        HelpMethods.fillTemperatureInfo(stringBuilder, weatherToSend);

        Wind wind = weatherToSend.getWind();
        String direction = HelpMethods.parseWind(wind);

        stringBuilder.append(EmojiParser.parseToUnicode("\n\n&#128168; *Ветер*: ") + direction);
        HelpMethods.fillFinalPartInfo(stringBuilder, weatherToSend);

        stringBuilder.append(EmojiParser.parseToUnicode("\n\n&#127749; *Восход солнца*: ") + HelpMethods.formatDate(datePattern, sunrise));
        stringBuilder.append(EmojiParser.parseToUnicode("\n&#127751; *Заход солнца*: ") + HelpMethods.formatDate(datePattern, sunset));

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), stringBuilder.toString());
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

    private SendMessage getForecastToChooseMessage(Message message, ForecastDTO forecastDTO) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Прогноз погоды в *" + forecastDTO.getCity().getName() + ":*");

        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), stringBuilder.toString());
        sendMessage.setReplyMarkup(forecastToChooseKeyBoard.setKeyBoardByDays(forecastDTO, message));
        sendMessage.enableMarkdown(true);

        return sendMessage;

    }

}
