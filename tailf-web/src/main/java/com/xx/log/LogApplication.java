package com.xx.log;

import com.xx.log.properties.LogProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableWebSocket
public class LogApplication implements InitializingBean {

    // 保存文件监听参数
    @Resource
    private LogProperties logProperties;

    // 保存启动参数
    private static String[] arg = null;

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("请使用类似: java -jar tailf-web-1.0-xxx.jar \"/usr/logs/tomcat1\" \"/usr/logs/tomcat2\" 命令启动！");
        } else {
            arg = args;
            SpringApplication.run(LogApplication.class);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化路径参数
        HashMap<String, String> map = new HashMap<String, String>();
        logProperties.setPath(map);
        AtomicInteger index = new AtomicInteger(1);
        Arrays.stream(arg).forEach(arg -> {
            map.put("Path" + index.getAndIncrement(), arg);
        });
    }
}