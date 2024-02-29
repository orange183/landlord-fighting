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
    private final EventLoopGroup group = new NioEventLoopGroup();

    @Override
    public void connect(String serverAddress, int port) throws InterruptedException {
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            SimplePrinter.printNotice("正在连接" + serverAddress + ":" + port);
            Channel channel = bootstrap.connect(serverAddress, port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ch.pipeline()
                    // 添加用于处理空闲状态的Handler，设置读写空闲时间为4秒
                    .addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
                    // 添加用于解码带有Google Protocol Buffers Varint32长度字段的消息的Handler
                    .addLast(new ProtobufVarint32FrameDecoder())
                    // 添加用于解码Protobuf消息的Handler，使用给定的Protobuf消息类型
                    .addLast(new ProtobufDecoder(ClientTransferData.ClientTransferDataProtoc.getDefaultInstance()))
                    // 添加用于在消息头部添加Google Protocol Buffers Varint32长度字段的Handler
                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                    // 添加用于编码Protobuf消息的Handler
                    .addLast(new ProtobufEncoder())
                    // 添加自定义的Protobuf编解码器的Handler
                    .addLast(new SecondProtobufCodec())
                    // 添加用于处理Protobuf消息传输的Handler
                    .addLast(new ProtobufTransferHandler());
        }
    }
}
