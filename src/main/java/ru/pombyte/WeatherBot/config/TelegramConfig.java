package ru.pombyte.WeatherBot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramConfig {

    @Value("${bot.webhook_path}")
    String webhookPath;
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botToken;

}
