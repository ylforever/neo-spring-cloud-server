package com.neo.springcloud.gateway;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关服务启动类
 *
 * @author neo
 * @since 2025/4/29
 * @version 1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NeoGateWayApp {
    private static final Logger LOGGER = LogManager.getLogger(NeoGateWayApp.class);

    public static void main(String[] args) {
        SpringApplication.run(NeoGateWayApp.class);
        LOGGER.info("Start up NeoGateWay success!");
    }
}
