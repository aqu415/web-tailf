package com.xx.log.properties;

import com.xx.log.common.enums.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储当前路径参数
 *
 * @author win10
 */
@Component
@Data
//@ConfigurationProperties(prefix = "tailf")
@EnableConfigurationProperties
public class AppProperties {

    // 本地监听目录，可以设置多个以 ; 隔开
    @Value("${tailf.monitor-path}")
    private String monitorPath;

    // 默认显示的文件行数
    @Value("${tailf.default-show-line-num}")
    private int defaultShowLineNum;

    // 当前应用的角色（master|slave）
    @Value("${tailf.web-role}")
    private String webRole;

    //如果当前应用角色是slave,则需要配置向master web注册IP
    @Value("${tailf.master-netty-host}")
    private String masterNettyHost;

    //如果当前应用角色是slave,则需要配置向master web注册netty端口
    @Value("${tailf.master-netty-port}")
    private int masterNettyPort;

    /**
     * 是否是master角色
     *
     * @return boolean
     */
    public boolean isMaster() {
        return (Role.MASTER.name().equalsIgnoreCase(this.getWebRole()));
    }

    /**
     * 是否是slave角色
     *
     * @return boolean
     */
    public boolean isSlave() {
        return Role.SLAVE.name().equalsIgnoreCase(this.getWebRole());
    }

}
