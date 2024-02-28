package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_MISMATCH extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);

        SimplePrinter.printNotice("你的组合是 " + map.get("playType") + " (" + map.get("playCount") + "), 但之前的组合是 " + map.get("preType") + " (" + map.get("preCount") + "). 不匹配!");

        if (lastPokers != null) {
            SimplePrinter.printNotice(lastSellClientNickname + "[" + lastSellClientType + "] played:");
            SimplePrinter.printPokers(lastPokers);
        }

        pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
    }

}
