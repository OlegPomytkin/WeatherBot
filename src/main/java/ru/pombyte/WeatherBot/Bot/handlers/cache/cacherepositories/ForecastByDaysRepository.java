package ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.ForecastByDaysCache;

@Repository
public interface ForecastByDaysRepository extends CrudRepository<ForecastByDaysCache,String> {
}
