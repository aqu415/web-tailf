package com.xx.log.monitor.local;

import com.xx.log.common.util.FileUtil;
import com.xx.log.common.util.MixUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * 处理监听目录及文件事件
 */
@Slf4j
public class MonitorListener extends FileAlterationListenerAdaptor {

    // 文件变化回调处理
    private MonitorCallback monitorCallback;

    public MonitorListener(MonitorCallback monitorCallback) {
        this.monitorCallback = monitorCallback;
    }

    /**
     * 文件创建
     */
    public void onFileCreate(File file) {
        log.info("onFileCreate" + file);
    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
        Long lastOffset = MonitorContext.getOffset(file.getAbsolutePath());
        if (lastOffset == null) {
            lastOffset = 0L;
        }
        if (this.monitorCallback == null) {
            log.warn("monitorCallback is null,please check!");
            return;
        }
        try {
            RandomAccessFile random = new RandomAccessFile(file, "r");
            random.seek(lastOffset);
            boolean end = false;
            while (!end) {
                String line = random.readLine();
                if (line == null) {
                    end = true;
                }
                if (!MixUtil.isEmpty(line)) {
                    String real = new String(FileUtil.getBytes(line.toCharArray()), "utf-8");
                    this.monitorCallback.change(file.getAbsolutePath(), real);
                }
            }
            random.close();
        } catch (Exception e) {
            log.error("onFileChange", e);
        }
        MonitorContext.setOffset(file.getAbsolutePath(), file.length());
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        log.info("onFileDelete" + file);
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        log.info("onDirectoryCreate" + directory);
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        log.info("onDirectoryChange" + directory);
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        log.info("onDirectoryDelete" + directory);
    }
}
