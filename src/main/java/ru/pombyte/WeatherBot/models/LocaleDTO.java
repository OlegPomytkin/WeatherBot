package ru.pombyte.WeatherBot.models;

import lombok.AllArgsConstructor;
import lombok.Data;


/**Модель города, который выбирается пользователем из запроса DTO**/
@Data
@AllArgsConstructor
public class LocaleDTO {
    private String name;
    private Double lat;
    private Double lon;
    private String country;
    private String state;

    @Override
    public String toString() {
        return "LocaleDTO{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +

                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
