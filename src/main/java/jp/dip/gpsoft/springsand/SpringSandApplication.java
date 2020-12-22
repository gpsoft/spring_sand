package jp.dip.gpsoft.springsand;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSandApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSandApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
		};
	}
}
