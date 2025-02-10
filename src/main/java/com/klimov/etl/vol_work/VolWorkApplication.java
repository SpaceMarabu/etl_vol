package com.klimov.etl.vol_work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VolWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolWorkApplication.class, args);
    }

}
