package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_CLIENT_KICK extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {

        SimplePrinter.printNotice("你因为无所事事而被赶出了房间。\n");

        get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
    }

}
