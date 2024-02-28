package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;

public class ClientEventListener_CODE_ROOM_CREATE_SUCCESS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Room room = Noson.convert(data, Room.class);
        initLastSellInfo();
        SimplePrinter.printNotice("您已经创建了一个房间,id为 " + room.getId());
        SimplePrinter.printNotice("请等待其他玩家加入!");
    }

}
