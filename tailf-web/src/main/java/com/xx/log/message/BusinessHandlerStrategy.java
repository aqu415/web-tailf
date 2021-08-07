package com.xx.log.message;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Markable;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.message.handler.BusinessHandler;
import com.xx.log.netty.handler.HandlerStrategy;
import com.xx.log.netty.handler.Msg;
import com.xx.log.netty.invoke.InvokeContext;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理netty消息的策略
 */
@Component
@Slf4j
public class BusinessHandlerStrategy implements HandlerStrategy {

    // messageHandlers
    @Resource
    private BusinessHandler[] businessHandlers;

    @Override
    public void handleMessage(ChannelHandlerContext ctx, String messageJson) {
        Markable message = JSONObject.parseObject(messageJson, Markable.class);
        MessageHead messageHead = message.getMessageHead();

        // 业务处理Handler
        BusinessHandler real = null;
        Message realMessage = null;
        for (BusinessHandler handler : businessHandlers) {
            Msg msg = AnnotationUtils.findAnnotation(handler.getClass(), Msg.class);
            if (msg.head() == message.getMessageHead()) {
                real = handler;
                if (MessageHead.RESPONSE == message.getMessageHead()) {
                    Class clazz = InvokeContext.getInvokeResult(message.getThreadId()).getResultMark();
                    realMessage = (Message)JSONObject.parseObject(messageJson, clazz);
                } else {
                    realMessage = JSONObject.parseObject(messageJson, msg.body());
                }
                break;
            }
        }

        // 执行真正的策略
        if (real != null) {
            real.handleMessage(ctx, realMessage);
        } else {
            log.warn("there`s not messageHandler to handle：" + messageHead);
        }
    }
}
