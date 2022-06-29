package com.vieira.sogolon.ItauChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ItauChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItauChallengeApplication.class, args);
	}

}
