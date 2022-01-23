package com.bananna.ninja.boostrap;

import com.bananna.ninja.handler.*;
import com.bananna.ninja.message.NinjaOperationMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Component
public class NettyBoostrap {

    public static int TCP_PORT = 6600;
    public static int UDP_PORT = 6700;

    @Resource
    NinjaEnterMessageHandler ninjaEnterMessageHandler;

    @Resource
    NinjaReadyMessageHandler ninjaReadyMessageHandler;

    @Resource
    NinjaOperationMessageHandler ninjaOperationMessageHandler;

    public void run(){
        EventLoopGroup mainGroup = new NioEventLoopGroup(2);
//        mainGroup.execute(this::runTcpServer);
        mainGroup.execute(this::runUdpServer);
    }

    private void runTcpServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(channelRegisterHandler);
//                            p.addLast(new JsonObjectDecoder());
//                            p.addLast(new StringDecoder());
//                            p.addLast(new StringEncoder());
//                            p.addLast(ninjaProtocolDecoder);
//                            p.addLast(dispatchHandler);
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(TCP_PORT).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void runUdpServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true) // todo why 可以不加吗
//                    // 设置读缓冲区为2M
//                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
//                    // 设置写缓冲区为1M
//                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        public void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new ChannelRefHandler());
                            p.addLast(new NinjaMessageDecoder());
                            p.addLast(new NinjaMessageEncoder());
                            p.addLast(ninjaEnterMessageHandler);
                            p.addLast(ninjaReadyMessageHandler);
                            p.addLast(ninjaOperationMessageHandler);
                        }
                    });

            // Start the server.
            ChannelFuture f = b.bind(UDP_PORT).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
        }
    }
}
