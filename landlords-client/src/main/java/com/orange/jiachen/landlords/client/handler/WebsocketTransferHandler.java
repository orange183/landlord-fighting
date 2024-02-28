package com.orange.jiachen.landlords.client.handler;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.client.event.ClientEventListener;
import com.orange.jiachen.landlords.entity.Msg;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.HashMap;
import java.util.Map;

public class WebsocketTransferHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        Msg msg = JsonUtils.fromJson(frame.text(), Msg.class);
        if (msg.getInfo() != null && !msg.getInfo().isEmpty()) {
            SimplePrinter.printNotice(msg.getInfo());
        }
        ClientEventCode code = ClientEventCode.valueOf(msg.getCode());
        if (User.INSTANCE.isWatching()) {
            Map<String, Object> wrapMap = new HashMap<>(3);
            wrapMap.put("code", code);
            wrapMap.put("data", msg.getData());

            ClientEventListener.get(ClientEventCode.CODE_GAME_WATCH).call(ctx.channel(), JsonUtils.toJson(wrapMap));
        } else {
            ClientEventListener.get(code).call(ctx.channel(), msg.getData());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ChannelUtils.pushToServer(ctx.channel(), ServerEventCode.CODE_CLIENT_HEAD_BEAT, "heartbeat");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.io.IOException) {
            SimplePrinter.printNotice("网络不好或者长时间没有运行，已经离线");
            System.exit(0);
        }
    }

}
