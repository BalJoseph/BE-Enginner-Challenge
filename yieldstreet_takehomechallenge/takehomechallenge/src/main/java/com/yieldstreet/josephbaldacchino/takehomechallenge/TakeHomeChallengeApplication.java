package com.yieldstreet.josephbaldacchino.takehomechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TakeHomeChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakeHomeChallengeApplication.class, args);
	}

}
