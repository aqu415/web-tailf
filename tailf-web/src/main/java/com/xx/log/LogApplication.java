package com.xx.log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import com.xx.log.properties.LogProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class LogApplication implements InitializingBean {

    // 保存文件监听参数
    @Resource
    private LogProperties logProperties;

    // 保存启动参数
    private static String[] arg = null;

    public static void main(String[] args) {
        if (args != null) {
            arg = args;
        }
        SpringApplication.run(LogApplication.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化路径参数
        HashMap<String, String> map = new HashMap<String, String>();
        logProperties.setPath(map);

        // 如果启动参数没有传值则通过配置文件获取
        if (arg == null || arg.length == 0) {
            arg = logProperties.getMonitorPath().split(";");
        }

        // 编号页面展示
        AtomicInteger index = new AtomicInteger(1);
        Arrays.stream(arg).forEach(ee -> {
            map.put("Path" + index.getAndIncrement(), ee);
        });
    }
}