package ru.pombyte.WeatherBot.Bot.handlers.cache;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pombyte.WeatherBot.Bot.States;
import ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories.ForecastByDaysRepository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories.ForecastCacheRepository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories.FoundCitiesCacheRepository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories.StateCacheRepository;
import ru.pombyte.WeatherBot.models.ForecastDTO;
import ru.pombyte.WeatherBot.models.LocaleDTO;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Кэш для состояния бота и списка городов по запросу
 **/

@Component
@Data
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotStateCache {
    final StateCacheRepository stateCacheRepository;
    final FoundCitiesCacheRepository foundCitiesCacheRepository;
    final ForecastCacheRepository forecastCacheRepository;
    final ForecastByDaysRepository forecastByDaysRepository;

    @Transactional
    public void saveBotState(String chatId, States botState) {

        StateCache stateCache = new StateCache(chatId, botState);
        stateCacheRepository.save(stateCache);

    }

    public States getBotState(String chatId) {

        StateCache state = stateCacheRepository.findById(chatId).orElse(null);
        return state != null ? state.getState() : States.NEW;

    }

    @Transactional
    public void saveFoundCities(String chatId, List<LocaleDTO> foundList) {

        FoundCitiesCache foundCitiesCache = new FoundCitiesCache(chatId, foundList);
        foundCitiesCacheRepository.save(foundCitiesCache);

    }

    public List<LocaleDTO> getFoundCities(String chatId) {

        return foundCitiesCacheRepository.findById(chatId).get().getLocaleList();

    }

    @Transactional
    public void saveForecast(String chatId, ForecastDTO forecast) {

        ForecastCache forecastCache = forecastCacheRepository.findById(chatId).orElse(null);

        if (forecastCache == null) {
            forecastCache = new ForecastCache(chatId, forecast);
            forecastCacheRepository.save(forecastCache);
        } else {
            forecastCache.setForecastDTO(forecast);
            forecastCache.setId(chatId);
        }

    }

    public ForecastDTO getForecast(String chatId) {

        ForecastCache forecastCache = forecastCacheRepository.findById(chatId).orElse(null);
        return forecastCache != null ? forecastCache.getForecastDTO() : null;

    }

    @Transactional
    public void saveDailyForecast(String userId, String day, List<WeatherDTO> weatherList) {

        ForecastByDaysCache fbdCache = forecastByDaysRepository.findById(userId).orElse(null);
        WeatherByDaysCache wbdCache = new WeatherByDaysCache(day, weatherList);

        if (fbdCache == null) {

            Set<WeatherByDaysCache> wbdCacheSet = new LinkedHashSet<>();
            wbdCacheSet.add(wbdCache);
            fbdCache = new ForecastByDaysCache(userId, wbdCacheSet);


        } else {

            Set<WeatherByDaysCache> wbdCacheSet = fbdCache.getWeatherByDaysCacheSet();
            wbdCacheSet.add(wbdCache);
            fbdCache.setWeatherByDaysCacheSet(wbdCacheSet);

        }

        forecastByDaysRepository.save(fbdCache);

    }

    public List<WeatherDTO> getForecastByDaysList(String chatId, String data) {

        ForecastByDaysCache fbdCache = forecastByDaysRepository.findById(chatId).orElse(null);

        if (fbdCache != null) {

            for (WeatherByDaysCache wbd : fbdCache.getWeatherByDaysCacheSet()) {
                if (wbd.getDay().equals(data))
                    return wbd.getForecastList();
            }

        }

        return null;

    }

    public Set<WeatherByDaysCache> getForecastSet(String chatId) {

        ForecastByDaysCache fbdCache = forecastByDaysRepository.findById(chatId).orElse(null);

        return fbdCache != null ? fbdCache.getWeatherByDaysCacheSet() : null;

    }

    @Transactional
    public void resetForecastCache (String chatId){

        forecastCacheRepository.deleteById(chatId);
        forecastByDaysRepository.deleteById(chatId);

    }

}
