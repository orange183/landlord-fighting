package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_EXIT extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);

        Integer exitClientId = (Integer) map.get("exitClientId");

        String role;
        if (exitClientId == SimpleClient.id) {
            role = "你";
        } else {
            role = String.valueOf(map.get("exitClientNickname"));
        }
        SimplePrinter.printNotice(role + " 离开房间. 房间关闭!\n");

        get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
    }
}
