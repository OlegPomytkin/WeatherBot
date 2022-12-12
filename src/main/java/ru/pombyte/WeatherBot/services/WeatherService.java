package ru.pombyte.WeatherBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pombyte.WeatherBot.clients.WeatherClient;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;

/**
 * Сервис используется только для работы с Feign клиентом, вся логика прописана в UserService
 **/
@Service
public class WeatherService {
    private final WeatherClient weatherClient;
    @Value("${weather.apikey}")
    private String apiKey;
    @Value("${weather.lang}")
    private String lang;

    @Autowired
    public WeatherService(WeatherClient weatherClient) {

        this.weatherClient = weatherClient;

    }

    public WeatherDTO requestWeatherData(Double lat, Double lon) {

        return weatherClient.getWeather(lat, lon, apiKey, lang);

    }

}
