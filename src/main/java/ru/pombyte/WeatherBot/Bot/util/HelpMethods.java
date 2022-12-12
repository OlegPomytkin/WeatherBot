package ru.pombyte.WeatherBot.Bot.util;

import com.vdurmont.emoji.EmojiParser;
import ru.pombyte.WeatherBot.Bot.States;
import ru.pombyte.WeatherBot.Bot.handlers.cache.BotStateCache;
import ru.pombyte.WeatherBot.models.Weather.WeatherClass;
import ru.pombyte.WeatherBot.models.Weather.WeatherDTO;
import ru.pombyte.WeatherBot.models.Weather.Wind;
import ru.pombyte.WeatherBot.models.Weather.enums.WeatherIcons;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**Вспомогательные методы (используются более 1 раза)**/
public class HelpMethods {

    private HelpMethods(){}

    public static String formatDate(String pattern, Date date){

        DateFormat df = new SimpleDateFormat(pattern, new Locale("ru"));
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        return df.format(date);

    }

    public static void changeBotState(BotStateCache botStateCache, String chatId, States botState){

        States newState = botState;
        botStateCache.saveBotState(chatId, newState);

    }

    /**Расчитывает направление ветра по градусам**/
    public static String parseWind(Wind wind){

        String direction;
        if (wind.getDegree() >= 11 && wind.getDegree() < 79)
            direction = "северо-восточный";
        else if (wind.getDegree() >= 79 && wind.getDegree() < 101)
            direction = "восточный";
        else if (wind.getDegree() >= 101 && wind.getDegree() < 169)
            direction = "юго-восточный";
        else if (wind.getDegree() >= 169 && wind.getDegree() < 191)
            direction = "южный";
        else if (wind.getDegree() >= 191 && wind.getDegree() < 259)
            direction = "юго-западный";
        else if (wind.getDegree() >= 259 && wind.getDegree() < 281)
            direction = "западный";
        else if (wind.getDegree() >= 281 && wind.getDegree() < 349)
            direction = "северо-западный";
        else direction = "северный";

        return direction;

    }

    /**Заполняет ответ по температуре**/
    public static void fillTemperatureInfo (StringBuilder infoUnit, WeatherDTO weatherDTO){

        for (WeatherClass weather : weatherDTO.getWeather())
            infoUnit.append(" - " + WeatherIcons.getIcon(weather.getId()) + " " + weather.getDescription() + ":");

        infoUnit.append(EmojiParser.parseToUnicode("\n\n&#127777; *Температура*: ") + weatherDTO.getMainReadings().getTemp() + " °C");
        infoUnit.append(EmojiParser.parseToUnicode("\n&#127777; *Температура* ощущается как: ") + weatherDTO.getMainReadings().getFeelsLike() + " °C");
        infoUnit.append(EmojiParser.parseToUnicode("\n&#127777; *Температура* мин: ") + weatherDTO.getMainReadings().getTempMin() + " °C");
        infoUnit.append(EmojiParser.parseToUnicode("\n&#127777; *Температура* макс: ") + weatherDTO.getMainReadings().getTempMax() + " °C");
        infoUnit.append(EmojiParser.parseToUnicode("\n\n&#9202; *Давление*: ") + weatherDTO.getMainReadings().getPressure() + " hPa");
        infoUnit.append(EmojiParser.parseToUnicode("\n&#128167; *Влажность*: ") + weatherDTO.getMainReadings().getHumidity() + " %");
        infoUnit.append(EmojiParser.parseToUnicode("\n&#127745; *Видимость*: ") + weatherDTO.getVisibility() + " м");

    }

    /**Заполняет вторую половину ответа по погоде**/
    public static void fillFinalPartInfo (StringBuilder infoUnit, WeatherDTO weatherDTO){

        Wind wind = weatherDTO.getWind();

        infoUnit.append(EmojiParser.parseToUnicode("\n&#128168; *Скорость ветра*: ") + wind.getSpeed() + " м/с");
        if (wind.getGust() != null && wind.getSpeed() < wind.getGust())
            infoUnit.append(EmojiParser.parseToUnicode("\n&#128168; С порывами до: ") + wind.getGust() + " м/с");

        infoUnit.append(EmojiParser.parseToUnicode("\n\n&#9729; *Облачность*: ") + weatherDTO.getClouds().getCloudiness() + " %");

        if (weatherDTO.getRain() != null) {
            if (weatherDTO.getRain().getOneHour() != null)
                infoUnit.append(EmojiParser.parseToUnicode("\n\n&#127783; *Осадки* за последний час: ") + weatherDTO.getRain().getOneHour() + " мм");
            if (weatherDTO.getRain().getThreeHour() != null)
                infoUnit.append(EmojiParser.parseToUnicode("\n&#127783; *Осадки* за последние 3 часа: ") + weatherDTO.getRain().getThreeHour() + " мм");
        }
        if (weatherDTO.getSnow() != null) {
            if (weatherDTO.getSnow().getOneHour() != null)
                infoUnit.append(EmojiParser.parseToUnicode("\n\n&#127784; *Осадки* за последний час: ") + weatherDTO.getSnow().getOneHour() + " мм");
            if (weatherDTO.getSnow().getThreeHour() != null)
                infoUnit.append(EmojiParser.parseToUnicode("\n&#127784; *Осадки* за последние 3 часа: ") + weatherDTO.getSnow().getThreeHour() + " мм");
        }
    }
}
