package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.server.ServerContains;

public class ServerEventListener_CODE_GAME_WATCH_EXIT implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {
        Room room = ServerContains.getRoom(clientSide.getRoomId());

        if (room != null) {
            // 房间如果存在，则将观战者从房间观战列表中移除
            clientSide.setRoomId(room.getId());
            boolean successful = room.getWatcherList().remove(clientSide);
            if (successful) {
                SimplePrinter.serverLog(clientSide.getNickname() + " exit room " + room.getId());
            }
        }
    }
}
