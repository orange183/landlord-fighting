package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_CONFIRM extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);

        String landlordNickname = String.valueOf(map.get("landlordNickname"));

        SimplePrinter.printNotice(landlordNickname + " 已经成为地主并获得了三张额外的卡");

        List<Poker> additionalPokers = Noson.convert(map.get("additionalPokers"), new NoType<List<Poker>>() {
        });
        SimplePrinter.printPokers(additionalPokers);

        pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
    }

}
