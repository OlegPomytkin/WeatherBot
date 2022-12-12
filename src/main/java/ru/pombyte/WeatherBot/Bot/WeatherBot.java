package ru.pombyte.WeatherBot.Bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.pombyte.WeatherBot.Bot.handlers.CallBackQueryHandler;
import ru.pombyte.WeatherBot.Bot.handlers.MessageHandler;
import ru.pombyte.WeatherBot.Bot.handlers.ScheduleTasksHandler;
import ru.pombyte.WeatherBot.Bot.keyboards.MainMenuKeyBoard;
import ru.pombyte.WeatherBot.services.ForecastService;
import ru.pombyte.WeatherBot.services.UserService;



/**
 * Инициализация бота
 **/

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherBot extends SpringWebhookBot {

    String botPath;
    String botUsername;
    String botToken;
    @Autowired
    MainMenuKeyBoard mainMenuKeyBoard;
    MessageHandler messageHandler;
    CallBackQueryHandler callBackQueryHandler;
    ScheduleTasksHandler scheduleTasksHandler;
    @Autowired
    ForecastService forecastService;
    @Autowired
    UserService userService;

    @Autowired
    public WeatherBot(SetWebhook setWebhook, MessageHandler messageHandler, CallBackQueryHandler callBackQueryHandler, ScheduleTasksHandler scheduleTasksHandler) {

        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callBackQueryHandler = callBackQueryHandler;
        this.scheduleTasksHandler = scheduleTasksHandler;

    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        try {

            return handleUpdate(update);

        } catch (IllegalArgumentException e) {

            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Неверная команда, пожалуйста, убедитесь в корректности ввода или воспользуйтесь кнопкой \"**Помощь**\"");

        } catch (Exception e) {

            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Ошибка выполнения, пожалуйста, повторите команду");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update){

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callBackQueryHandler.processCallbackQuery(callbackQuery);

        } else {
            Message message = update.getMessage();

            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }

        return null;
    }

    @Scheduled(cron = "${scheduler.timeset}")
    private void hadleWeatherAutoNotification(){

        scheduleTasksHandler.handleTask(this);

    }

}




