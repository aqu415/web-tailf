package com.xx.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "log-config")
@Data
public class LogProperties {
    private HashMap<String, String> path;
}
