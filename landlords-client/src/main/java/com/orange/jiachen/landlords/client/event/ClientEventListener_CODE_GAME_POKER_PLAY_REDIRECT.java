package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.FormatPrinter;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_REDIRECT extends ClientEventListener {

    private static String[] choose = new String[]{"UP", "DOWN"};

    private static String format = "\n[%-4s] %-" + ClientEventListener_CODE_CLIENT_NICKNAME_SET.NICKNAME_MAX_LENGTH + "s  剩余 %-2s [%-8s]";

    @SuppressWarnings("unchecked")
    @Override
    public void call(Channel channel, String data) {

        Map<String, Object> map = MapHelper.parser(data);

        int sellClientId = (int) map.get("sellClientId");

        List<Map<String, Object>> clientInfos = (List<Map<String, Object>>) map.get("clientInfos");

        for (int index = 0; index < 2; index++) {
            for (Map<String, Object> clientInfo : clientInfos) {
                String position = (String) clientInfo.get("position");
                if (position.equalsIgnoreCase(choose[index])) {
                    FormatPrinter.printNotice(format, clientInfo.get("position"), clientInfo.get("clientNickname"), clientInfo.get("surplus"), clientInfo.get("type"));
                }
            }
        }
        SimplePrinter.printNotice("");

        if (sellClientId == SimpleClient.id) {
            get(ClientEventCode.CODE_GAME_POKER_PLAY).call(channel, data);
        } else {
            SimplePrinter.printNotice("It is " + map.get("sellClientNickname") + "'s turn. 请等他把牌打出来.");
        }
    }

}
