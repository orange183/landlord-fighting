package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_GAME_LANDLORD_CYCLE extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("没有玩家拿地主，所以重新发牌。");

    }

}
