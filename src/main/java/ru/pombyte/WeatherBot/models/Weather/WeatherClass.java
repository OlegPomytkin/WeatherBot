package ru.pombyte.WeatherBot.models.Weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherClass {

    @JsonProperty("id")
    Integer id;
    @JsonProperty("main")
    String main;
    @JsonProperty("description")
    String description;
    @JsonProperty("icon")
    String icon;

}
