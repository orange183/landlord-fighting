package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ClientStatus;
import com.orange.jiachen.landlords.enums.RoomStatus;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.server.ServerContains;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerEventListener_CODE_ROOM_JOIN implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {

        Room room = ServerContains.getRoom(Integer.parseInt(data));

        if (room == null) {
            String result = MapHelper.newInstance()
                    .put("roomId", data)
                    .json();
            ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST, result);
            return;
        }
        if (room.getClientSideList().size() == 3) {
            String result = MapHelper.newInstance()
                    .put("roomId", room.getId())
                    .put("roomOwner", room.getRoomOwner())
                    .json();
            ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_FULL, result);
            return;
        }
        // join default ready
        clientSide.setStatus(ClientStatus.READY);
        clientSide.setRoomId(room.getId());

        ConcurrentSkipListMap<Integer, ClientSide> roomClientMap = (ConcurrentSkipListMap<Integer, ClientSide>) room.getClientSideMap();
        LinkedList<ClientSide> roomClientList = room.getClientSideList();

        if (roomClientList.size() > 0) {
            ClientSide pre = roomClientList.getLast();
            pre.setNext(clientSide);
            clientSide.setPre(pre);
        }

        roomClientList.add(clientSide);
        roomClientMap.put(clientSide.getId(), clientSide);

        if (roomClientMap.size() == 3) {
            clientSide.setNext(roomClientList.getFirst());
            roomClientList.getFirst().setPre(clientSide);

            ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, String.valueOf(room.getId()));
            return;
        }
        room.setStatus(RoomStatus.WAIT);
        String result = MapHelper.newInstance()
                .put("clientId", clientSide.getId())
                .put("clientNickname", clientSide.getNickname())
                .put("roomId", room.getId())
                .put("roomOwner", room.getRoomOwner())
                .put("roomClientCount", room.getClientSideList().size())
                .json();
        for (ClientSide client : roomClientMap.values()) {
            ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_ROOM_JOIN_SUCCESS, result);
        }

        notifyWatcherJoinRoom(room, clientSide);
    }

    /**
     * 通知观战者玩家加入房间
     *
     * @param room       房间
     * @param clientSide 玩家
     */
    private void notifyWatcherJoinRoom(Room room, ClientSide clientSide) {
        for (ClientSide watcher : room.getWatcherList()) {
            ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_ROOM_JOIN_SUCCESS, clientSide.getNickname());
        }
    }
}
