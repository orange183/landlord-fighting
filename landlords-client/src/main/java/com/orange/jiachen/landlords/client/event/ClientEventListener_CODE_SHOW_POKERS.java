package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_SHOW_POKERS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {

        Map<String, Object> map = MapHelper.parser(data);

        lastSellClientNickname = (String) map.get("clientNickname");
        lastSellClientType = (String) map.get("clientType");

        SimplePrinter.printNotice(lastSellClientNickname + "[" + lastSellClientType + "] played:");
        lastPokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        });
        SimplePrinter.printPokers(lastPokers);

        if (map.containsKey("sellClientNickname")) {
            SimplePrinter.printNotice("下一位出牌人是" + map.get("sellClientNickname") + "。 请等他打好他的组合。");
        }
    }

}
