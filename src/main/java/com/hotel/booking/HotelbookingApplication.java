package com.hotel.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HotelbookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelbookingApplication.class, args);
    }

}
