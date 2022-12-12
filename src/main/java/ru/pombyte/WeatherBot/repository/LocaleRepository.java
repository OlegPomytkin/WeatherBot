package ru.pombyte.WeatherBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.models.Locale;
import ru.pombyte.WeatherBot.models.User;
import java.util.Optional;
@Repository
public interface LocaleRepository extends CrudRepository<Locale,Long> {

    Optional<Locale> findByUser(User user);

}
