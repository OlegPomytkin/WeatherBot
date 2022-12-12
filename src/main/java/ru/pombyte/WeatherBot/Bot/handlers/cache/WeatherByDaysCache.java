package ru.pombyte.WeatherBot.Bot.handlers.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherByDaysCache {

    String day;
    List<WeatherDTO> forecastList;
}
