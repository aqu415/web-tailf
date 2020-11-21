package com.xx.log.moniter;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
public class Agent {

    /**
     * 给目录加监听
     * 
     * @param file
     */
    public FileAlterationMonitor registerMonitor(String file) {
    	
    	// 当前文件
    	File current = new File(file);
    	
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(3);
        
        // 创建过滤器
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter txtFiles = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"));
        IOFileFilter logFiles = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".log"));
        IOFileFilter filter = FileFilterUtils.or(directories, txtFiles, logFiles);
        
        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(current.getParent(), filter);
        observer.addListener(new FileMoniter());
        
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        
        // 开始监控
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monitor;
    }
}
