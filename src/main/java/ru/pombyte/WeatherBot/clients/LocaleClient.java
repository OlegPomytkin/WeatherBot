package ru.pombyte.WeatherBot.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pombyte.WeatherBot.clients.config.LocaleClientConfig;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import java.util.List;

/**
 * Клиент для запроса геолокации через API
 */
@FeignClient(name = "locale-client", url = "http://api.openweathermap.org/geo/1.0/direct", configuration = LocaleClientConfig.class)
public interface LocaleClient {

    @GetMapping("?q={q}&limit=${weather.limitcities}&appid={key}")
    List<LocaleDTO> getLocaleByName(@RequestParam("q") String query, @RequestParam("key") String apiKey);

}
