package ru.pombyte.WeatherBot.Bot.handlers.cache.cacherepositories;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.Bot.handlers.cache.StateCache;

@Repository
public interface StateCacheRepository extends CrudRepository<StateCache,String> {
}
