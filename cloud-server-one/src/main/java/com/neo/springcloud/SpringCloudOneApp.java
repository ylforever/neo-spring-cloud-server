package com.neo.springcloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 微服务启动程序
 *
 * @author neo
 * @since 2025/3/26
 * @version 1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class SpringCloudOneApp {
    private static final Logger LOGGER = LogManager.getLogger(SpringCloudOneApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudOneApp.class);
        LOGGER.info("Start up SpringCloudOneApp success.");
    }
}
