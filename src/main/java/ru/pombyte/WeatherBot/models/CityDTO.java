package ru.pombyte.WeatherBot.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityDTO {

    String name;
    Long timezone;
    Long sunrise;
    Long sunset;

}
