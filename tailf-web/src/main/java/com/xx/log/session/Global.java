package com.xx.log.session;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Global {

    //保存全部slave连接
    private static Map<String/*host*/, ChannelHandlerContext> channel = new ConcurrentHashMap<>();

    // 全局ip和对应的监听路径
    private static Map<String/*host*/, String/*path1;path2*/> hostAndPath = new ConcurrentHashMap<>();

    public static void register(String host, String path, ChannelHandlerContext ctx) {
        if (host != null) {
            hostAndPath.put(host, path);
            if (ctx != null) {
                channel.put(host, ctx);
            }
        }
    }

    public static Map<String/*host*/, String/*path1;path2*/> getRegisterInfo() {
        return new ConcurrentHashMap<>(hostAndPath);
    }

    public static ChannelHandlerContext getChannelContext(String host) {
        return channel.get(host);
    }
}
