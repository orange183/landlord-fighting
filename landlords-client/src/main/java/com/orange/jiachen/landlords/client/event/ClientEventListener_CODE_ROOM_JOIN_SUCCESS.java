package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_SUCCESS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);

        initLastSellInfo();

        int joinClientId = (int) map.get("clientId");
        if (SimpleClient.id == joinClientId) {
            SimplePrinter.printNotice("你加入了房间id：" + map.get("roomId") + ". There are " + map.get("roomClientCount") + " 玩家正在房间中.");
            SimplePrinter.printNotice("请等待其他玩家加入。游戏将从三个玩家开始!");
        } else {
            SimplePrinter.printNotice(map.get("clientNickname") + " joined room, there are currently " + map.get("roomClientCount") + " in the room.");
        }

    }
}
