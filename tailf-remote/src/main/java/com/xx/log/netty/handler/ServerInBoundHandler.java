package com.xx.log.netty.handler;

import com.xx.log.common.enums.Role;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerInBoundHandler extends AbstractInBoundHandler {

    /**
     * construct
     *
     * @param role
     */
    public ServerInBoundHandler(Role role, HandlerStrategy handlerStrategy) {
        super(role, handlerStrategy);
    }

    @Override
    void active(ChannelHandlerContext ctx) {

    }
}
