package com.xx.log.message.handler.server;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.common.pojo.message.RegisterMsg;
import com.xx.log.message.handler.BusinessHandler;
import com.xx.log.netty.handler.Msg;
import com.xx.log.session.Global;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理客户端注册请求的处理器
 */
@Slf4j
@Component
@Msg(head = MessageHead.REGISTER, body = RegisterMsg.class)
public class RegisterHandler implements BusinessHandler {

    @Override
    public void handleMessage(ChannelHandlerContext ctx, Message message) {
        log.info("RegisterHandler.handleMessage:" + message);
        RegisterMsg registerMsg = (RegisterMsg) message;
        Global.register(registerMsg.getHost(), registerMsg.getPath(), ctx);
    }
}
