package ru.pombyte.WeatherBot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForecastDTO {

    @JsonProperty("list")
    List<WeatherDTO> forecastList;
    CityDTO city;

}
