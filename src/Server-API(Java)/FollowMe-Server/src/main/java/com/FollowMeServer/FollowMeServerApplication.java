package com.FollowMeServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.FollowMeServer")
public class FollowMeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FollowMeServerApplication.class, args);
	}

}
