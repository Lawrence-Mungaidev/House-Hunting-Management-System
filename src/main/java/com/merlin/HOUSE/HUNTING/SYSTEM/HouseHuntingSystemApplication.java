package com.merlin.HOUSE.HUNTING.SYSTEM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HouseHuntingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseHuntingSystemApplication.class, args);
	}

}
