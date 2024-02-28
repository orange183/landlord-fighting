package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.print.FormatPrinter;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;

import java.util.List;
import java.util.Map;

import static com.orange.jiachen.landlords.client.event.ClientEventListener_CODE_CLIENT_NICKNAME_SET.NICKNAME_MAX_LENGTH;

public class ClientEventListener_CODE_SHOW_ROOMS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {

        List<Map<String, Object>> roomList = Noson.convert(data, new NoType<List<Map<String, Object>>>() {
        });
        if (roomList != null && !roomList.isEmpty()) {
            // "COUNT" begins after NICKNAME_MAX_LENGTH characters. The dash means that the string is left-justified.
            String format = "#\t%s\t|\t%-" + NICKNAME_MAX_LENGTH + "s\t|\t%-6s\t|\t%-6s\t#\n";
            FormatPrinter.printNotice(format, "ID", "OWNER", "COUNT", "TYPE");
            for (Map<String, Object> room : roomList) {
                FormatPrinter.printNotice(format, room.get("roomId"), room.get("roomOwner"), room.get("roomClientCount"), room.get("roomType"));
            }
            SimplePrinter.printNotice("");
            get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
        } else {
            SimplePrinter.printNotice("没有可用的房间了。请创建一个房间!");
            get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
        }
    }
}
