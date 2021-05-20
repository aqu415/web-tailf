package com.xx.log.netty.handler;

import com.xx.log.common.enums.MessageHead;
import com.xx.log.common.enums.Role;
import com.xx.log.common.pojo.message.RegisterMsg;
import com.xx.log.common.util.IPUtil;
import com.xx.log.netty.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientInboundHandler extends AbstractInBoundHandler {

    // 监听路径
    private String[] paths;
    private int port;

    /**
     * construct
     *
     * @param role
     */
    public ClientInboundHandler(Role role, NettyClient.NettyClientParam param) {
        super(role, param.getHandlerStrategy());
        this.paths = param.getPaths();
        this.port = param.getPort();
    }

    @Override
    void active(ChannelHandlerContext ctx) {
        RegisterMsg message = RegisterMsg.builder()
                .path(String.join(";", this.paths))
                .host(IPUtil.getLocalHost() + ":" + this.port)
                .build();
        message.setMessageHead(MessageHead.REGISTER);
        this.writeAndFlushMessage(message);
    }
}
