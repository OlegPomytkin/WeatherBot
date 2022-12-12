package ru.pombyte.WeatherBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pombyte.WeatherBot.clients.LocaleClient;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import java.util.List;

/**Сервис используется только для работы с Feign клиентом, вся логика прописана в UserService**/
@Service
public class LocaleService {

    @Value("${weather.apikey}")
    private String apiKey;
    private final LocaleClient localeClient;

    @Autowired
    public LocaleService(LocaleClient localeClient) {

        this.localeClient = localeClient;

    }

    public List<LocaleDTO> requestLocale (String query) {

        return localeClient.getLocaleByName(query,apiKey);

    }

}
