package com.example.teamStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringBootProjectApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringBootProjectApplication.class);

    public static void main(String[] args) {
        log.info("Starting TeamStack Application...");
        SpringApplication.run(SpringBootProjectApplication.class, args);
        log.info("TeamStack Application Started Successfully!");
    }

}
