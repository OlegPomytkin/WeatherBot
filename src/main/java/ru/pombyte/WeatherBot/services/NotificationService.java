package ru.pombyte.WeatherBot.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.pombyte.WeatherBot.models.Notification;
import ru.pombyte.WeatherBot.models.User;
import ru.pombyte.WeatherBot.repository.NotificationRepository;



import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationService {

    final UserService userService;
    final NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications () {

        return notificationRepository.findAll();

    }

    @Transactional
    public void save (Message message, Long offset, String time) {

        User user = userService.findOne(message);
        Notification notification = notificationRepository.findByUser(user).orElse(null);

        if(notification == null)
            notification = new Notification();

        notification.setTimezone(offset);
        notification.setTime(time);

        notification.setUser(user);
        notificationRepository.save(notification);

        user.setNotification(notification);

    }

    @Transactional
    public void resetNotification (Message message) {

        User user = userService.findOne(message);
        Notification notification = notificationRepository.findByUser(user).orElse(null);

        if(notification != null)
            notificationRepository.deleteById(notification.getId());

    }

}
