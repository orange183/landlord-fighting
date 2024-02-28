package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_GAME_POKER_PLAY_LESS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("你的组合排名比之前的低。你不能玩这种组合!");

        if (lastPokers != null) {
            SimplePrinter.printNotice(lastSellClientNickname + "[" + lastSellClientType + "] played:");
            SimplePrinter.printPokers(lastPokers);
        }

        pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
    }

}
