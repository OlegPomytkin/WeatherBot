package ru.pombyte.WeatherBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pombyte.WeatherBot.clients.ForecastClient;
import ru.pombyte.WeatherBot.models.ForecastDTO;

@Service
public class ForecastService {

    private final ForecastClient forecastClient;
    @Value("${weather.apikey}")
    private String apiKey;
    @Value("${weather.lang}")
    private String lang;

    @Autowired
    public ForecastService(ForecastClient forecastClient) {
        this.forecastClient = forecastClient;
    }

    public ForecastDTO requestForecastData(Double lat, Double lon) {

        return forecastClient.getForecast(lat, lon, apiKey, lang);

    }

}
