package com.orange.jiachen.landlords.client.proxy;

import com.orange.jiachen.landlords.client.handler.ProtobufTransferHandler;
import com.orange.jiachen.landlords.client.handler.SecondProtobufCodec;
import com.orange.jiachen.landlords.entity.ClientTransferData;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ProtobufProxy implements Proxy {
    @Override
    public void connect(String serverAddress, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(ClientTransferData.ClientTransferDataProtoc.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new SecondProtobufCodec())
                                    .addLast(new ProtobufTransferHandler());
                        }
                    });
            SimplePrinter.printNotice("正在连接 " + serverAddress + ":" + port);
            Channel channel = bootstrap.connect(serverAddress, port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
