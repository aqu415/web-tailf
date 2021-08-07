package com.xx.log.session;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenyang
 * @version 1.6.0
 * @date 2020.10.26 10:13
 * @since 1.6.0
 */
@Slf4j
public class SessionContext {

    /**
     * 目录订阅关系
     */
    private static Map<String/** path */, Set<String/** host */>> subscription = new ConcurrentHashMap<>();

    /*** 记录所有的连接 */
    private static Map<String/** host */, Session> allClient = new ConcurrentHashMap<>();


    /**
     * 注册host,session
     */
    public synchronized static void register(String host, Session session) {
        allClient.put(host, session);
    }

    /**
     * 监听某某文件
     *
     * @param path
     * @return
     */
    public synchronized static void subscribe(String host, String path, String searchKey) {
        Set<String> hosts = subscription.computeIfAbsent(path, k -> new HashSet<>());
        hosts.add(host);
    }

    /*** 登出 */
    public synchronized static void unRegister(final String host) {
        // 删除连接
        allClient.remove(host);

        // 删除"目录订阅关系"
        subscription.values().forEach(set -> {
            set.remove(host);
        });
    }

    /*** 广播 */
    public static void sendMsg(String line) {
        allClient.values().forEach((session) -> {
            try {
                session.getBasicRemote().sendText(line);
            } catch (Exception e) {
                log.error("广播通知客户端异常：", e);
            }
        });
    }

    /**
     * 某文件内容发生变化，通知客户端
     */
    public static void sendMsg(String path, String line) {
        Set<String> hosts = subscription.get(path);
        if (hosts != null) {
            hosts.forEach((host) -> {
                Optional.ofNullable(allClient.get(host)).ifPresent((session) -> {
                    sendMsg(session, line);
                });
            });
        }
    }

    public static void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.error("通知客户端异常：", e);
        }
    }
}
