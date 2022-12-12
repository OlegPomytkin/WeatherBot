package ru.pombyte.WeatherBot.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pombyte.WeatherBot.clients.config.LocaleClientConfig;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;

/**
 * Клиент для запроса текущей погоды через API
 */
@FeignClient(name = "weather-client", url = "https://api.openweathermap.org/data/2.5/weather", configuration = LocaleClientConfig.class)
public interface WeatherClient {

    @GetMapping("?lat={lat}&lon={lon}&lang={lang}&units=metric&appid={key}")
    WeatherDTO getWeather(@RequestParam("lat") Double lat, @RequestParam("lon") Double lon, @RequestParam("key") String apiKey, @RequestParam("lang") String lang);

}
