package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_ELECT extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);
        int turnClientId = (int) map.get("nextClientId");

        if (map.containsKey("preClientNickname")) {
            SimplePrinter.printNotice(map.get("preClientNickname") + " don't rob the landlord!");
        }

        if (turnClientId == SimpleClient.id) {
            SimplePrinter.printNotice("轮到你了。你想抢地主吗?[Y/N](输入[exit|e]退出当前房间)");
            String line = SimpleWriter.write(User.INSTANCE.getNickname(), "Y/N");
            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
                pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
            } else if (line.equalsIgnoreCase("Y")) {
                pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "TRUE");
            } else if (line.equalsIgnoreCase("N")) {
                pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "FALSE");
            } else {
                SimplePrinter.printNotice("无效的选项");
                call(channel, data);
            }
        } else {
            SimplePrinter.printNotice("现在是 " + map.get("nextClientNickname") + "的回合. 请耐心等待他的确认 !");
        }

    }

}
