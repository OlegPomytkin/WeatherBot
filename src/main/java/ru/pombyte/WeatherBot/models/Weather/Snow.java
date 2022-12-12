package ru.pombyte.WeatherBot.models.Weather;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Snow {

    Double oneHour;
    Double threeHour;

}
