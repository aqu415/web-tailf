package com.xx.log.netty.handler;

import com.xx.log.common.pojo.message.Message;
import io.netty.channel.ChannelHandlerContext;

public interface HandlerStrategy {

    void handleMessage(ChannelHandlerContext ctx, String messageJson);
}
