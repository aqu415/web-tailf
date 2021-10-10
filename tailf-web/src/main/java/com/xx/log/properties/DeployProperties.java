package com.xx.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储当前路径参数
 *
 * @author win10
 */
@Component
@Data
@ConfigurationProperties(prefix = "tailf")
@EnableConfigurationProperties
public class DeployProperties {


    // 自动部署时附件文件所在的目录
    private String attachmentFilepath;

    // 自动部署时附件文件的名称
    private String attachmentFileName;

}
