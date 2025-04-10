package com.neo.springcloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringClientApp {
    private static final Logger LOGGER = LogManager.getLogger(SpringClientApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringClientApp.class);
        LOGGER.info("Start up SpringClientApp success.");
    }
}
