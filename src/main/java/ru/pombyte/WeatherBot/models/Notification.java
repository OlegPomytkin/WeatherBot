package ru.pombyte.WeatherBot.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="notification")
@NoArgsConstructor
public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "chat_id")
    private User user;

    @Column(name = "time")
    private String time;

    @Column(name = "timezone")
    private Long timezone;

}
