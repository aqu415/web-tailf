package com.xx.log.message.handler;

import com.xx.log.common.pojo.message.Message;
import io.netty.channel.ChannelHandlerContext;

public interface BusinessHandler {

    void handleMessage(ChannelHandlerContext ctx, Message message);
}
