package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_ROOM_PLAY_FAIL_BY_INEXIST extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {

        SimplePrinter.printNotice("游戏失败。房间已经解散了!");
        ClientEventListener.get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
    }


}
