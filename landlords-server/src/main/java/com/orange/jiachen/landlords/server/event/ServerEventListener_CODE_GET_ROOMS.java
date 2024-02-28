package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.server.ServerContains;
import org.nico.noson.Noson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServerEventListener_CODE_GET_ROOMS implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {
        List<Map<String, Object>> roomList = new ArrayList<>(ServerContains.getRoomMap().size());
        for (Entry<Integer, Room> entry : ServerContains.getRoomMap().entrySet()) {
            Room room = entry.getValue();
            roomList.add(MapHelper.newInstance()
                    .put("roomId", room.getId())
                    .put("roomOwner", room.getRoomOwner())
                    .put("roomClientCount", room.getClientSideList().size())
                    .put("roomType", room.getType())
                    .map());
        }
        ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_SHOW_ROOMS, Noson.reversal(roomList));
    }

}
