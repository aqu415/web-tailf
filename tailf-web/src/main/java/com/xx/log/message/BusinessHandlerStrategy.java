package com.xx.log.message;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Markable;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.common.util.MixUtil;
import com.xx.log.message.handler.BusinessHandler;
import com.xx.log.netty.handler.HandlerStrategy;
import com.xx.log.netty.handler.Msg;
import com.xx.log.netty.invoke.InvokeContext;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        Markable message = MixUtil.jsonStr2Obj(messageJson, Markable.class);
        MessageHead messageHead = message.getMessageHead();

        // 业务处理Handler
        BusinessHandler real = null;
        Message realMessage = null;
        for (BusinessHandler handler : businessHandlers) {
            Msg msg = AnnotationUtils.findAnnotation(handler.getClass(), Msg.class);
            Assert.notNull(msg, String.format("%s 应该有@Msg注解，但是没有找到！", handler.getClass().getName()));
            if (msg.head() == message.getMessageHead()) {
                real = handler;
                if (MessageHead.RESPONSE == message.getMessageHead()) {
                    Class clazz = InvokeContext.getInvokeResult(message.getThreadId()).getResultMark();
                    realMessage = (Message) MixUtil.jsonStr2Obj(messageJson, clazz);
                } else {
                    realMessage = MixUtil.jsonStr2Obj(messageJson, msg.body());
                }
                break;
            }
        }

        // 执行真正的策略
        if (real != null) {
            real.handleMessage(ctx, realMessage);
        } else {
            log.warn("there`s no messageHandler to handle：" + messageHead);
        }
    }
}
