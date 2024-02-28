package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ClientRole;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_OFFLINE implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {

        Room room = ServerContains.getRoom(clientSide.getRoomId());

        if (room == null) {
            ServerContains.CLIENT_SIDE_MAP.remove(clientSide.getId());
            return;
        }

        String result = MapHelper.newInstance()
                .put("roomId", room.getId())
                .put("exitClientId", clientSide.getId())
                .put("exitClientNickname", clientSide.getNickname())
                .json();
        for (ClientSide client : room.getClientSideList()) {
            if (client.getRole() != ClientRole.PLAYER) {
                continue;
            }
            if (client.getId() != clientSide.getId()) {
                ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, result);
                client.init();
            }
        }
        ServerContains.removeRoom(room.getId());
    }
}
