package com.xx.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EnableConfigurationProperties
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class);
    }
}