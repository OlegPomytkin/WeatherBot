package ru.pombyte.WeatherBot.Bot.handlers.cache;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;
import ru.pombyte.WeatherBot.Bot.States;

@RedisHash("statecache")
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateCache {

    String id;
    States state;
}
