package ru.pombyte.WeatherBot.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Locale locale;

    @OneToOne(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Notification notification;

}
