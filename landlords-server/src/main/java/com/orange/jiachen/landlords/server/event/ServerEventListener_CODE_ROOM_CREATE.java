package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ClientStatus;
import com.orange.jiachen.landlords.enums.RoomStatus;
import com.orange.jiachen.landlords.enums.RoomType;
import com.orange.jiachen.landlords.server.ServerContains;
import org.nico.noson.Noson;

public class ServerEventListener_CODE_ROOM_CREATE implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {

        Room room = new Room(ServerContains.getServerId());
        room.setStatus(RoomStatus.WAIT);
        room.setType(RoomType.PVP);
        room.setRoomOwner(clientSide.getNickname());
        room.getClientSideMap().put(clientSide.getId(), clientSide);
        room.getClientSideList().add(clientSide);
        room.setCurrentSellClient(clientSide.getId());
        room.setCreateTime(System.currentTimeMillis());
        room.setLastFlushTime(System.currentTimeMillis());

        clientSide.setRoomId(room.getId());
        ServerContains.addRoom(room);

        clientSide.setStatus(ClientStatus.NO_READY);

        ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_CREATE_SUCCESS, Noson.reversal(room));
    }
}
