package ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.ForecastCache;
import ru.pombyte.WeatherBot.Bot.handlers.cache.FoundCitiesCache;

@Repository
public interface ForecastCacheRepository extends CrudRepository<ForecastCache,String> {
}
