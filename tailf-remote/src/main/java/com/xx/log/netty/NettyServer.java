package com.xx.log.netty;

import com.xx.log.common.enums.Role;
import com.xx.log.common.util.ConstanceUtil;
import com.xx.log.netty.handler.AbstractInBoundHandler;
import com.xx.log.netty.handler.HandlerStrategy;
import com.xx.log.netty.handler.ServerInBoundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyServer {
    private AbstractInBoundHandler handler;
    private ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupSelector;
    private final EventLoopGroup eventLoopGroupBoss;
    private int port = 0;


    public NettyServer(int port) {
        this.port = port;
        this.serverBootstrap = new ServerBootstrap();

        // boss
        this.eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyBoss_%d", this.threadIndex.incrementAndGet()));
            }
        });

        // worker
        int threadCount = 4;
//        if (RemotingUtil.isLinuxPlatform()) {
//            this.eventLoopGroupSelector = new EpollEventLoopGroup(threadCount, new ThreadFactory() {
//                private AtomicInteger threadIndex = new AtomicInteger(0);
//                private int threadTotal = threadCount;
//
//                @Override
//                public Thread newThread(Runnable r) {
//                    return new Thread(r, String.format("NettyServerEPOLLSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
//                }
//            });
//        } else {
            this.eventLoopGroupSelector = new NioEventLoopGroup(threadCount, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);
                private int threadTotal = threadCount;

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerNIOSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
                }
            });
//        }
    }

    public void start(HandlerStrategy handlerStrategy) {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupSelector).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .localAddress(new InetSocketAddress(port))
                .childHandler(this.getChannelInitializer(handlerStrategy));
        try {
            this.serverBootstrap.bind().sync();
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }
        log.info(">>>NettyServer started,port:" + port);
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer(HandlerStrategy handlerStrategy) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                ByteBuf delimiter = Unpooled.copiedBuffer(ConstanceUtil.DECODER_DELIMITER.getBytes());
                pipeline.addLast(new DelimiterBasedFrameDecoder(2048, delimiter));

                // 字符串解码 和 编码
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                //handler
                handler = new ServerInBoundHandler(Role.MASTER, handlerStrategy);
                pipeline.addLast(handler);
            }
        };
    }
}
