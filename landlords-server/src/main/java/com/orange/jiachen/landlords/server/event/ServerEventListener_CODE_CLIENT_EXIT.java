package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ClientRole;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_EXIT implements ServerEventListener {

    private static final Object locked = new Object();

    @Override
    public void call(ClientSide clientSide, String data) {
        synchronized (locked) {
            Room room = ServerContains.getRoom(clientSide.getRoomId());
            if (room == null) {
                return;
            }
            String result = MapHelper.newInstance()
                    .put("roomId", room.getId())
                    .put("exitClientId", clientSide.getId())
                    .put("exitClientNickname", clientSide.getNickname())
                    .json();
            for (ClientSide client : room.getClientSideList()) {
                if (client.getRole() == ClientRole.PLAYER) {
                    ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, result);
                    client.init();
                }
            }

            notifyWatcherClientExit(room, clientSide);
            ServerContains.removeRoom(room.getId());
        }
    }

    /**
     * 通知所有观战者玩家退出游戏
     *
     * @param room   房间
     * @param player 退出游戏玩家
     */
    private void notifyWatcherClientExit(Room room, ClientSide player) {
        for (ClientSide watcher : room.getWatcherList()) {
            ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, player.getNickname());
        }
    }
}
