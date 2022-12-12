package ru.pombyte.WeatherBot.models.Weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainReadings {

    @JsonProperty("temp")
    Double temp;
    @JsonProperty("feels_like")
    Double feelsLike;
    @JsonProperty("temp_min")
    Double tempMin;
    @JsonProperty("temp_max")
    Double tempMax;
    @JsonProperty("pressure")
    Integer pressure;
    @JsonProperty("humidity")
    Double humidity;

}
