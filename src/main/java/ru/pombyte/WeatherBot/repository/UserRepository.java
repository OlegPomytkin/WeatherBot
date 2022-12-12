package ru.pombyte.WeatherBot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.models.User;
@Repository
public interface UserRepository extends CrudRepository<User,Long> {}
