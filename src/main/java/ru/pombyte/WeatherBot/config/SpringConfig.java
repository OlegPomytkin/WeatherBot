package ru.pombyte.WeatherBot.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pombyte.WeatherBot.Bot.WeatherBot;
import ru.pombyte.WeatherBot.Bot.handlers.CallBackQueryHandler;
import ru.pombyte.WeatherBot.Bot.handlers.MessageHandler;
import ru.pombyte.WeatherBot.Bot.handlers.ScheduleTasksHandler;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SpringConfig {
    @NonNull
    private final TelegramConfig telegramConfig;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.host}")
    private String redisHost;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }
    @Bean
    public WeatherBot weatherBot (SetWebhook setWebhook, MessageHandler messageHandler, CallBackQueryHandler callBackQueryHandler, ScheduleTasksHandler scheduleTasksHandler){

        WeatherBot bot = new WeatherBot(setWebhook, messageHandler, callBackQueryHandler, scheduleTasksHandler);

        bot.setBotPath(telegramConfig.getWebhookPath());
        bot.setBotToken(telegramConfig.getBotToken());
        bot.setBotUsername(telegramConfig.getBotName());

        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Запустить или перезапустить бота"));
        listofCommands.add(new BotCommand("/notify", "Настроить или отключить автоматические уведомления о погоде"));
        listofCommands.add(new BotCommand("/weather", "Узнать погоду на данный момент"));
        listofCommands.add(new BotCommand("/forecast", "Узнать 6 часовой прогноз на 5 дней"));
        listofCommands.add(new BotCommand("/city", "Изменить город"));

        try {
            bot.execute(new SetMyCommands(listofCommands,new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return bot;

    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(redisHost);
        jedisConFactory.setPort(redisPort);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}
