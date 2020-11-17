package com.xx.log.moniter;

import com.xx.log.util.SpringUtil;
import org.apache.commons.io.monitor.FileAlterationMonitor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorContext {

    /***目录监听器*/
    private static Map<String/**dir*/, FileAlterationMonitor/**monitor*/> fileMonitor = new ConcurrentHashMap<>();

    /***文件读取位置*/
    private static Map<String /***file*/, Long/***line offset*/> fileLine = new ConcurrentHashMap<>();

    /**
     * 注册监目录听器
     *
     * @param path
     */
    public static void register(String path) {
        File file = new File(path);
        if (file.exists() && fileMonitor.get(file.getParent()) == null) {
            Agent agent = SpringUtil.getBean(Agent.class);
            FileAlterationMonitor monitor = agent.registerMonitor(path);
            fileMonitor.put(file.getParent(), monitor);
        }
    }

    /**
     * 获得文件读取位置
     *
     * @param filePath
     * @return
     */
    public static Long getOffset(String filePath) {
        return fileLine.get(filePath);
    }

    /**
     * 设置文件读取位置
     *
     * @param filePath
     * @param offset
     */
    public static void setOffset(String filePath, Long offset) {
        fileLine.put(filePath, offset);
    }
}
