package ru.pombyte.WeatherBot.Bot.handlers.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import java.util.List;

@RedisHash("foundcitiescache")
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoundCitiesCache {

    String id;
    List<LocaleDTO> localeList;
}
