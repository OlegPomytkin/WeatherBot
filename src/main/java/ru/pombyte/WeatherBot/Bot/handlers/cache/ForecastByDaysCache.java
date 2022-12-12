package ru.pombyte.WeatherBot.Bot.handlers.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;


import java.util.Set;

@RedisHash(value="dailycache", timeToLive = 43200L)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForecastByDaysCache {

    String id;
    Set<WeatherByDaysCache> weatherByDaysCacheSet;
}
