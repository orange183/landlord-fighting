package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_GAME_READY extends ClientEventListener {
    static void gameReady(Channel channel) {
        SimplePrinter.printNotice("\n你想继续游戏吗? [Y/N]");
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "notReady");
        if (line.equals("Y") || line.equals("y")) {
            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_GAME_READY, "");
            return;
        }
        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT, "");
    }

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);
        if (SimpleClient.id == (int) map.get("clientId")) {
            SimplePrinter.printNotice("你准备好玩游戏了。");
            return;
        }
        SimplePrinter.printNotice(map.get("clientNickName").toString() + " 准备好玩游戏了。");
    }
}
