package com.daysources.thirdchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication @EnableFeignClients
public class ThirdchallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdchallengeApplication.class, args);
    }

}
