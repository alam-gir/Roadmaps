package com.roadmaps.Roadmaps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableRetry
public class RoadmapsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadmapsApplication.class, args);
	}

}
