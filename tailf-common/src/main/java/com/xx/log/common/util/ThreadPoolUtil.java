package com.xx.log.common.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public class ThreadPoolUtil {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 提交异步任务
     *
     * @param task Runnable
     */
    public static void submit(Runnable task) {
        executor.submit(task);
    }
}
