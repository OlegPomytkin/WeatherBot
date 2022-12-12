package ru.pombyte.WeatherBot.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pombyte.WeatherBot.clients.config.LocaleClientConfig;
import ru.pombyte.WeatherBot.models.ForecastDTO;

/**
 * Клиент для запроса прогноза погоды через API
 */
@FeignClient(name = "forecast-client", url = "http://api.openweathermap.org/data/2.5/forecast", configuration = LocaleClientConfig.class)
public interface ForecastClient {
    @GetMapping("?lat={lat}&lon={lon}&lang={lang}&units=metric&appid={key}")
    ForecastDTO getForecast(@RequestParam("lat") Double lat, @RequestParam("lon") Double lon, @RequestParam("key") String apiKey, @RequestParam("lang") String lang);

}
