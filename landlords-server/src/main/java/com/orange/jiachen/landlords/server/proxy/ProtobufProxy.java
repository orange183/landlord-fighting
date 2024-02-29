package com.orange.jiachen.landlords.server.proxy;

import com.orange.jiachen.landlords.entity.ServerTransferData;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.robot.RobotDecisionMakers;
import com.orange.jiachen.landlords.server.ServerContains;
import com.orange.jiachen.landlords.server.handler.ProtobufTransferHandler;
import com.orange.jiachen.landlords.server.handler.SecondProtobufCodec;
import com.orange.jiachen.landlords.server.timer.RoomClearTask;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ProtobufProxy implements Proxy {
    @Override
    public void start(int port) throws InterruptedException {
        EventLoopGroup parentGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        EventLoopGroup childGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(60 * 30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(ServerTransferData.ServerTransferDataProtoc.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new SecondProtobufCodec())
                                    .addLast(new ProtobufTransferHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind().sync();

            SimplePrinter.serverLog("protobuf服务器启动成功，端口为：" + port);
            // 初始化机器人决策器
            RobotDecisionMakers.init();
            // 启动定时任务
            ServerContains.THREAD_EXCUTER.execute(() -> {
                Timer timer = new Timer();
                timer.schedule(new RoomClearTask(), 0L, 3000L);
            });
            f.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
