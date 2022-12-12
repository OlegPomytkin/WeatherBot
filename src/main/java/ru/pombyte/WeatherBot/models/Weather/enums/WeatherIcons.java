package ru.pombyte.WeatherBot.models.Weather.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vdurmont.emoji.EmojiParser;

import java.io.Serializable;

/**
 * Икнонки
 **/
public enum WeatherIcons implements Serializable {

    THUNDERSTORM_200(200, EmojiParser.parseToUnicode("&#9928;")),
    THUNDERSTORM_201(201, EmojiParser.parseToUnicode("&#9928;")),
    THUNDERSTORM_202(202, EmojiParser.parseToUnicode("&#9928;")),
    THUNDERSTORM_210(210, EmojiParser.parseToUnicode("&#127785;")),
    THUNDERSTORM_211(211, EmojiParser.parseToUnicode("&#127785;")),
    THUNDERSTORM_212(212, EmojiParser.parseToUnicode("&#127785;")),
    THUNDERSTORM_221(221, EmojiParser.parseToUnicode("&#127785;")),
    THUNDERSTORM_230(230, EmojiParser.parseToUnicode("&#9928;")),
    THUNDERSTORM_231(231, EmojiParser.parseToUnicode("&#9928;")),
    THUNDERSTORM_232(232, EmojiParser.parseToUnicode("&#9928;")),

    DRIZZLE_300(300, EmojiParser.parseToUnicode("&#9730;")),
    DRIZZLE_301(301, EmojiParser.parseToUnicode("&#9730;")),
    DRIZZLE_302(302, EmojiParser.parseToUnicode("&#9748;")),
    DRIZZLE_310(310, EmojiParser.parseToUnicode("&#9748;")),
    DRIZZLE_311(311, EmojiParser.parseToUnicode("&#127782;")),
    DRIZZLE_312(312, EmojiParser.parseToUnicode("&#127783;")),
    DRIZZLE_313(313, EmojiParser.parseToUnicode("&#127783;")),
    DRIZZLE_314(314, EmojiParser.parseToUnicode("&#127783;")),
    DRIZZLE_321(321, EmojiParser.parseToUnicode("&#127783;")),

    RAIN_500(500, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_501(501, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_502(502, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_503(503, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_504(504, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_511(511, EmojiParser.parseToUnicode(":snowflake:")),
    RAIN_520(520, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_521(521, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_522(522, EmojiParser.parseToUnicode("&#127783;")),
    RAIN_531(531, EmojiParser.parseToUnicode("&#127783;")),

    SNOW_600(600, EmojiParser.parseToUnicode(":snowflake:")),
    SNOW_601(601, EmojiParser.parseToUnicode(":snowflake:")),
    SNOW_602(602, EmojiParser.parseToUnicode(":snowflake:")),
    SNOW_611(611, EmojiParser.parseToUnicode(":snowflake:")),
    SNOW_612(612, EmojiParser.parseToUnicode("&#127783;")),
    SNOW_613(613, EmojiParser.parseToUnicode("&#127783;")),
    SNOW_615(615, EmojiParser.parseToUnicode("&#127783;")),
    SNOW_616(616, EmojiParser.parseToUnicode("&#127783;")),
    SNOW_620(620, EmojiParser.parseToUnicode("&#127784;")),
    SNOW_621(621, EmojiParser.parseToUnicode("&#127784;")),
    SNOW_622(622, EmojiParser.parseToUnicode("&#127784;")),

    MIST(701, EmojiParser.parseToUnicode(":fog:")),
    SMOKE(711, EmojiParser.parseToUnicode(":fog:")),
    HAZE(721, EmojiParser.parseToUnicode(":foggy:")),
    DUST(731, EmojiParser.parseToUnicode(":foggy:")),
    FOG(741, EmojiParser.parseToUnicode(":fog:")),
    SAND(751, EmojiParser.parseToUnicode(":fog:")),
    DUST_2(761, EmojiParser.parseToUnicode(":fog:")),
    ASH(762, EmojiParser.parseToUnicode(":volcano:")),
    SQUALL(771, EmojiParser.parseToUnicode("&#128168;")),
    TORNADO(781, EmojiParser.parseToUnicode("&#127786;")),

    CLEAR(800, EmojiParser.parseToUnicode(":sun_with_face:")),

    CLOUDS_801(801, EmojiParser.parseToUnicode("&#127780;")),
    CLOUDS_802(802, EmojiParser.parseToUnicode("&#9925;")),
    CLOUDS_803(803, EmojiParser.parseToUnicode("&#127781;")),
    CLOUDS_804(804, EmojiParser.parseToUnicode(":cloud:"));

    private final Integer id;
    private final String description;

    WeatherIcons(int id, String description) {

        this.id = id;
        this.description = description;

    }

    /**
     * Первоначально состояния парсились как enum
     **/
    @Deprecated
    @JsonCreator
    public static WeatherIcons forValues(@JsonProperty("id") Integer id, @JsonProperty("main") String main,
                                         @JsonProperty("description") String description, @JsonProperty("icon") String icon) {

        for (WeatherIcons weather : WeatherIcons.values()) {
            if (weather.id.equals(id))
                return weather;
        }
        return null;

    }

    public static String getIcon(Integer id) {

        for (WeatherIcons weather : WeatherIcons.values()) {
            if (weather.id.equals(id))
                return weather.description;
        }
        return null;

    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
