package com.xx.log;

import com.xx.log.common.util.IPUtil;
import com.xx.log.netty.NettyClient;
import com.xx.log.netty.NettyServer;
import com.xx.log.netty.handler.HandlerStrategy;
import com.xx.log.properties.AppProperties;
import com.xx.log.session.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.Resource;

@SpringBootApplication
@EnableWebSocket
@Slf4j
public class LogApplication implements InitializingBean {

    // 保存文件监听参数
    @Resource
    private AppProperties appProperties;

    //master handlerStrategy
    @Resource
    private HandlerStrategy handlerStrategy;


    @Value("${server.port}")
    private int port;


    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 使用springboot配置获得策略：参数 --tailf.monitor-path=d:/log 优先级高于配置文件里的默认配置
        String[] paths = this.appProperties.getMonitorPath().split(";");

        // master|slave
        if (this.appProperties.isMaster()) {
            NettyServer nettyServer = new NettyServer(this.appProperties.getMasterNettyPort());
            nettyServer.start(handlerStrategy);

            // 将master监听的路径注册到
            Global.register(IPUtil.getLocalHost() + ":" + port, this.appProperties.getMonitorPath(), null);

        } else if (this.appProperties.isSlave()) {
            NettyClient nettyClient = new NettyClient(this.appProperties.getMasterNettyHost(), this.appProperties.getMasterNettyPort());
            nettyClient.start(NettyClient.NettyClientParam.builder().paths(paths).port(port).handlerStrategy(handlerStrategy).build());

        } else {
            throw new RuntimeException("please config server role(MASTER|SLAVE)");
        }
    }
}