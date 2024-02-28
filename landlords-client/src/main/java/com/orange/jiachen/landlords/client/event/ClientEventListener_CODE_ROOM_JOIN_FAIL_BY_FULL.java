package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_FULL extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> dataMap = MapHelper.parser(data);

        SimplePrinter.printNotice("加入房间失败。 房间号 " + dataMap.get("roomId") + " 是满的!");
        ClientEventListener.get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
    }


}
