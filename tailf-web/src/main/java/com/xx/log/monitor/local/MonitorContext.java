package com.xx.log.monitor.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 全局资源管理
 */
@Slf4j
public class MonitorContext {

    /***目录监听器*/
    private static Map<String/**path*/, FileAlterationMonitor/**monitor*/> fileMonitor = new ConcurrentHashMap<>();

    /***文件读取位置*/
    private static Map<String /***path*/, Long/***line offset*/> fileLine = new ConcurrentHashMap<>();

    /**
     * 注册监目录听器（对配置的目录进行注册监听器）
     *
     * @param path
     */
    public synchronized static void register(String path, MonitorCallback monitorCallback) {
        File file = new File(path);
        //todo 判断是否是本地路径
        if (file.exists() && fileMonitor.get(file.getParent()) == null) {
            FileAlterationMonitor monitor = registerMonitor(path, monitorCallback);
            fileMonitor.put(file.getParent(), monitor);
        }
    }

    /**
     * 给目录加监听
     *
     * @param file
     */
    private static FileAlterationMonitor registerMonitor(String file, MonitorCallback monitorCallback) {

        // 当前文件
        File current = new File(file);

        // 轮询间隔（s）
        long interval = TimeUnit.SECONDS.toMillis(2);

        // 创建过滤器
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter txtFiles = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"));
        IOFileFilter logFiles = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".log"));
        IOFileFilter filter = FileFilterUtils.or(directories, txtFiles, logFiles);

        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(current.getParent(), filter);
        observer.addListener(new MonitorListener(monitorCallback));

        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);

        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            log.error("monitor.start() exception!", e);
        }
        return monitor;
    }

    /**
     * 取消监听（什么时候合理？）
     *
     * @param path
     */
    public synchronized static void unRegister(String path) {
        FileAlterationMonitor monitor = fileMonitor.remove(path);
        try {
            monitor.stop();
        } catch (Exception e) {
            log.error("unRegister Exception:", e);
        }
        fileLine.remove(path);
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
