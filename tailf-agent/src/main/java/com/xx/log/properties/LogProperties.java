package com.xx.log.properties;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 存储当前路径参数
 * 
 * @author win10
 *
 */
@Component
@Data
@ConfigurationProperties(prefix = "log-config")
@EnableConfigurationProperties
public class LogProperties {

	/**监听的路径*/
	private HashMap<String, String> path;

	/**默认显示的文件行数**/
	private int defaultShowLineNum;
}
