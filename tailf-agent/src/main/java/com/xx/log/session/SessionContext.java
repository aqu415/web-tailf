package com.xx.log.session;

import javax.websocket.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	private static Map<String/** path */, Set<String/** host */>> subscription = new HashMap<>();

	/*** 记录所有的连接 */
	private static Map<String/** host */, Session> allClient = new HashMap<>();

	/**
	 * 注册
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
	public synchronized static void subscribe(String host, String path) {
		Set<String> hosts = subscription.get(path);
		if (hosts == null) {
			hosts = new HashSet<>();
			subscription.put(path, hosts);
		}
		hosts.add(host);
	}

	/*** 登出 */
	public synchronized static void unRegister(String host) {
		allClient.remove(host);
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

	/** 某文件内容发生变化，通知客户端 */
	public static void sendMsg(String path, String line) {
		Set<String> hosts = subscription.get(path);
		if(hosts != null) {
			hosts.forEach((host) -> {
				Optional.ofNullable(allClient.get(host)).ifPresent((session)->{
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
