package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_GAME_POKER_PLAY_CANT_PASS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("你出了前一张牌，所以你过不了。");
        pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
    }

}
