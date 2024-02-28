package com.orange.jiachen.landlords.client.proxy;

import com.orange.jiachen.landlords.client.handler.WebsocketTransferHandler;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class WebsocketProxy implements Proxy {
    @Override
    public void connect(String serverAddress, int port) throws InterruptedException, URISyntaxException {
        URI uri = new URI("ws://" + serverAddress + ":" + port + "/ratel");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(60 * 30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(8192))
                                    .addLast(new WebSocketClientProtocolHandler(uri
                                            , WebSocketVersion.V13
                                            , null
                                            , true
                                            , new DefaultHttpHeaders(), 100000))
                                    .addLast("ws", new WebsocketTransferHandler());
                        }
                    });
            SimplePrinter.printNotice("Connecting to " + serverAddress + ":" + port);
            Channel channel = bootstrap.connect(serverAddress, port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
