package ru.pombyte.WeatherBot.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**Модель города, который выбирается пользователем из запроса**/

@Data
@Entity
@Table(name="locales")
@NoArgsConstructor
public class Locale {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "chat_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

}
