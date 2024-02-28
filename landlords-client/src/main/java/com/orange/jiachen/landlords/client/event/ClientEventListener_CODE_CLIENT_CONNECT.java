package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;

import java.util.HashMap;
import java.util.Map;

public class ClientEventListener_CODE_CLIENT_CONNECT extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("已连接到服务器。 欢迎来到瑞特尔!");
        SimpleClient.id = Integer.parseInt(data);

        Map<String, Object> infos = new HashMap<>();
        infos.put("version", SimpleClient.VERSION);
        pushToServer(channel, ServerEventCode.CODE_CLIENT_INFO_SET, Noson.reversal(infos));
    }

}
