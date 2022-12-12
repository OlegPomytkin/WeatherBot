package ru.pombyte.WeatherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pombyte.WeatherBot.models.Notification;
import ru.pombyte.WeatherBot.models.User;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Optional<Notification> findByUser(User user);

}
