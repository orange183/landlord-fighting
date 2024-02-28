package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_GAME_POKER_PLAY_ORDER_ERROR extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {

        SimplePrinter.printNotice("还没轮到你呢。请等待其他玩家!");
    }

}
