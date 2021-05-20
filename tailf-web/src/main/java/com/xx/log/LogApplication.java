package com.xx.log;

import com.xx.log.common.util.IPUtil;
import com.xx.log.netty.NettyClient;
import com.xx.log.netty.NettyServer;
import com.xx.log.netty.handler.HandlerStrategy;
import com.xx.log.properties.LogProperties;
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
    private LogProperties logProperties;

    //master handlerStrategy
    @Resource
    private HandlerStrategy handlerStrategy;


    @Value("${server.port}")
    private int port;

    // 保存启动参数
    private static String[] paths = null;

    public static void main(String[] args) {
        if (args != null) {
            paths = args;
        }
        SpringApplication.run(LogApplication.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 如果启动参数没有传值则通过配置文件获取
        if (paths == null || paths.length == 0) {
            paths = this.logProperties.getMonitorPath().split(";");
        } else {
            this.logProperties.setMonitorPath(String.join(";", paths));
        }

        // master|slave
        if (this.logProperties.isMaster()) {
            NettyServer nettyServer = new NettyServer(this.logProperties.getMasterNettyPort());
            nettyServer.start(handlerStrategy);

            // 将master监听的路径注册到
            Global.register(IPUtil.getLocalHost() + ":" + port, this.logProperties.getMonitorPath(), null);

        } else if (this.logProperties.isSlave()) {
            NettyClient nettyClient = new NettyClient(this.logProperties.getMasterNettyHost(), this.logProperties.getMasterNettyPort());
            nettyClient.start(NettyClient.NettyClientParam.builder().paths(paths).port(port).handlerStrategy(handlerStrategy).build());

        } else {
            throw new RuntimeException("please config server role(MASTER|SLAVE)");
        }
    }
}