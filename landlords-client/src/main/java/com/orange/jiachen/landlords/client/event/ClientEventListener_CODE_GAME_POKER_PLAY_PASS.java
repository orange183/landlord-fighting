package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_PASS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);

        SimplePrinter.printNotice(map.get("clientNickname") + " 跳过. 现在是" + map.get("nextClientNickname") + "的回合.");

        int turnClientId = (int) map.get("nextClientId");
        if (SimpleClient.id == turnClientId) {
            pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
        }
    }

}
