package com.xx.log.message.handler.common;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.message.handler.BusinessHandler;
import com.xx.log.netty.handler.Msg;
import com.xx.log.netty.invoke.InvokeContext;
import com.xx.log.netty.invoke.InvokeResult;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理同步调用[即通过 com.xx.log.netty.invoke.InvokeContext#invoke()调用的返回值]返回值的处理器
 */
@Slf4j
@Component
@Msg(head = MessageHead.RESPONSE)
public class ResponseHandler implements BusinessHandler {

    @Override
    public void handleMessage(ChannelHandlerContext ctx, Message message) {
        Long threadId = message.getThreadId();
        InvokeResult invokeResult = InvokeContext.getInvokeResult(threadId);
        invokeResult.setData(message);
        invokeResult.getCountDownLatch().countDown();
    }
}
