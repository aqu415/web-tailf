package com.xx.log.netty.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 处理netty消息的策略
 */
public interface HandlerStrategy {

    void handleMessage(ChannelHandlerContext ctx, String messageJson);
}
