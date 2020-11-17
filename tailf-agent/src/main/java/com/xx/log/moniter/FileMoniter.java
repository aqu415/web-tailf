package com.xx.log.moniter;

import com.xx.log.util.ContentUtil;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.RandomAccessFile;

public class FileMoniter extends FileAlterationListenerAdaptor {

    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        System.out.println("onFileCreate" + file);
    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
        Long lastOffset = MonitorContext.getOffset(file.getAbsolutePath());
        if (lastOffset == null) {
            lastOffset = 0L;
        }
        try {
            RandomAccessFile random = new RandomAccessFile(file, "r");
            random.seek(lastOffset);
            boolean end = false;
            while (!end) {
            	String line = random.readLine();
                if(line == null) {
                	end = true;
                }
                if(!StringUtils.isEmpty(line)) {
                	String real = new String(ContentUtil.getBytes(line.toCharArray()), "utf-8");
                	com.xx.log.session.SessionContext.sendMsg(file.getAbsolutePath(), real);
                }
            }
            random.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MonitorContext.setOffset(file.getAbsolutePath(), file.length());
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        System.out.println("onFileDelete" + file);
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate" + directory);
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        System.out.println("onDirectoryChange" + directory);
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete" + directory);
    }
}
