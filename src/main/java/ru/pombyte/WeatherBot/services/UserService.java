package ru.pombyte.WeatherBot.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.pombyte.WeatherBot.models.Locale;
import ru.pombyte.WeatherBot.models.User;
import ru.pombyte.WeatherBot.repository.LocaleRepository;
import ru.pombyte.WeatherBot.repository.UserRepository;
import java.sql.Timestamp;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final UserRepository userRepository;
    final LocaleRepository localeRepository;

    public User findOne(Message message){

        return userRepository.findById(message.getChatId()).orElse(null);

    }

    @Transactional
    public void save(Message message) {

        User user = new User();

        user.setChatId(message.getChatId());
        user.setUserName(message.getChat().getUserName());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastName(message.getChat().getLastName());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

    }

    @Transactional
    public void save(Message message, Locale city) {

        User user = new User();
        Locale cityToSave = city;

        user.setChatId(message.getChatId());
        user.setUserName(message.getChat().getUserName());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastName(message.getChat().getLastName());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        cityToSave.setUser(user);
        localeRepository.save(cityToSave);

        user.setLocale(city);
        userRepository.save(user);

    }

    @Transactional
    public void updateCity(Message message, Locale city) {

        User user = findOne(message);
        Locale cityToUpdate = city;

        cityToUpdate.setUser(user);
        localeRepository.save(cityToUpdate);
        user.setLocale(cityToUpdate);

    }

    @Transactional
    public void deleteCity (Message message) {

        User user = findOne(message);
        Locale locale = user.getLocale();

        localeRepository.deleteById(locale.getId());

    }

}
