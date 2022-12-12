package ru.pombyte.WeatherBot;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class WeatherBotApplication {
	@Bean
	public ModelMapper modelMapper(){
	return  new ModelMapper();
}

	public static void main(String[] args) {

		SpringApplication.run(WeatherBotApplication.class, args);

	}



}
