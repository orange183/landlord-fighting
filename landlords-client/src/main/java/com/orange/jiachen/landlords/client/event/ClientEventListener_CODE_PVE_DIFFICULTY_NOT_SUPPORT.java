package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_PVE_DIFFICULTY_NOT_SUPPORT extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("当前难度不支持，请注意以下内容。\n");
        get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
    }


}
