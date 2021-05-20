package com.xx.log.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.xx.log.common.enums.Role;
import com.xx.log.common.pojo.message.Message;
import com.xx.log.common.util.ConstanceUtil;
import com.xx.log.netty.RemotingUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractInBoundHandler extends ChannelInboundHandlerAdapter {

    protected Role role;

    protected HandlerStrategy handlerStrategy;

    private ChannelHandlerContext context;

    protected AbstractInBoundHandler(Role role, HandlerStrategy handlerStrategy) {
        this.role = role;
        this.handlerStrategy = handlerStrategy;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelActive:" + ctx);
        this.context = ctx;
        this.active(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        log.debug(this.role + " channelRead:" + body);
        this.handlerStrategy.handleMessage(ctx, body);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive:" + ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught:" + ctx, cause);
        ctx.close();
    }

    /**
     * 发生字符串(末尾加上标记)
     *
     * @param msg
     */
    public void writeAndFlushStr(String msg) {
        this.context.writeAndFlush(msg + ConstanceUtil.DECODER_DELIMITER);
    }

    /**
     * 发送 Message
     *
     * @param message
     */
    public void writeAndFlushMessage(Message message) {
        RemotingUtil.writeAndFlushMessage(context, message);
    }

    /**
     * 激活
     *
     * @param ctx
     */
    abstract void active(ChannelHandlerContext ctx);
}
