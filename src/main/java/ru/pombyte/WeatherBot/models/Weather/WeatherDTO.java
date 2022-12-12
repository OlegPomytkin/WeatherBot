package ru.pombyte.WeatherBot.models.Weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherDTO {

    List<WeatherClass> weather;
    @JsonProperty("main")
    MainReadings mainReadings;
    Integer visibility;
    Wind wind;
    Rain rain;
    Snow snow;
    Clouds clouds;
    @JsonProperty("dt")
    Long time;
    Sys sys;
    String name;
    Long timezone;

}
