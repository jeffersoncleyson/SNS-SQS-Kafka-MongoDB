package br.com.sensedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SnsCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsCollectorApplication.class, args);
	}

}
