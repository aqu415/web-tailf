package com.xx.log.session;

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
public class SessionContext {

    /**
     * 目录订阅关系
     */
    private static Map<String/** path */, Set<String/** host */>> subscription = new ConcurrentHashMap<>();

    /*** 记录所有的连接 */
    private static Map<String/** host */, Session> allClient = new ConcurrentHashMap<>();

    /**
     * 存放每个客户端+文件对应的关键字
     */
    /*private static Map<String*//**host + path*//*, String*//**模糊搜索关键字*//*> clinetSearchKey = new ConcurrentHashMap<>();*/

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
        Set<String> hosts = subscription.get(path);
        if (hosts == null) {
            hosts = new HashSet<>();
            subscription.put(path, hosts);
        }
        hosts.add(host);

        /*if (searchKey != null && searchKey.length() > 0) {
            clinetSearchKey.put(host + path, searchKey);
        } else {
            clinetSearchKey.remove(host + path);
        }*/
    }

    /*** 登出 */
    public synchronized static void unRegister(final String host) {
        // 删除连接
        allClient.remove(host);

        // 删除"目录订阅关系"
        subscription.values().forEach(set -> {
            set.remove(host);
        });

        // 删除模糊搜索key
        /*Iterator<String> iterator = clinetSearchKey.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(host)) {
                clinetSearchKey.remove(key);
            }
        }*/
    }

    /*** 广播 */
    public static void sendMsg(String line) {
        allClient.values().forEach((a) -> {
            try {
                a.getBasicRemote().sendText(line);
            } catch (Exception e) {
                e.printStackTrace();
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
                    try {
                        session.getBasicRemote().sendText(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }
}
