package com.xx.log.netty;

import com.xx.log.common.enums.Role;
import com.xx.log.netty.handler.AbstractInBoundHandler;
import com.xx.log.netty.handler.ClientInboundHandler;
import com.xx.log.common.util.ConstanceUtil;
import com.xx.log.netty.handler.HandlerStrategy;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private Bootstrap bootstrap;
    private NioEventLoopGroup NIO_GROUP = new NioEventLoopGroup();
    private String masterNettyHost;
    private int masterNettyPort;
    private AbstractInBoundHandler handler;

    public NettyClient(String masterNettyHost, int masterNettyPort) {
        bootstrap = new Bootstrap();
        this.masterNettyHost = masterNettyHost;
        this.masterNettyPort = masterNettyPort;
    }

    /**
     * 启动服务，注册监听路径
     *
     * @param param
     */
    public void start(NettyClientParam param) {
        bootstrap.group(NIO_GROUP)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(this.getChannelInitializer(param));
        try {
            // 启动客户端
            this.bootstrap.connect(masterNettyHost, masterNettyPort).sync(); // (5)
            log.info("netty client start successfully");
        } catch (InterruptedException e) {
            log.error("com.xx.log.netty.NettyClient.start InterruptedException", e);
        }
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer(NettyClientParam param) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                ByteBuf delimiter = Unpooled.copiedBuffer(ConstanceUtil.DECODER_DELIMITER.getBytes());
                pipeline.addLast(new DelimiterBasedFrameDecoder(2048, delimiter));

                // 字符串解码 和 编码
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                // client解析器
                handler = new ClientInboundHandler(Role.SLAVE, param);
                pipeline.addLast(handler);
            }
        };
    }

    @Data
    @Builder
    public static class NettyClientParam {
        private String[] paths;
        private int port;
        private HandlerStrategy handlerStrategy;
    }
}
