package com.xx.log.monitor.local;

/**
 * 处理内容变化
 */
@FunctionalInterface
public interface MonitorCallback {

    void change(String fileAbsPath, String content);
}
