package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ClientRole;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.server.ServerContains;
import com.orange.jiachen.landlords.server.robot.RobotEventListener;

public class ServerEventListener_CODE_GAME_POKER_PLAY_PASS implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {
        Room room = ServerContains.getRoom(clientSide.getRoomId());

        if (room != null) {
            if (room.getCurrentSellClient() == clientSide.getId()) {
                if (clientSide.getId() != room.getLastSellClient()) {
                    ClientSide turnClient = clientSide.getNext();

                    room.setCurrentSellClient(turnClient.getId());

                    for (ClientSide client : room.getClientSideList()) {
                        String result = MapHelper.newInstance()
                                .put("clientId", clientSide.getId())
                                .put("clientNickname", clientSide.getNickname())
                                .put("nextClientId", turnClient.getId())
                                .put("nextClientNickname", turnClient.getNickname())
                                .json();
                        if (client.getRole() == ClientRole.PLAYER) {
                            ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_PASS, result);
                        } else {
                            if (client.getId() == turnClient.getId()) {
                                RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(turnClient, data);
                            }
                        }
                    }

                    notifyWatcherPlayPass(room, clientSide);
                } else {
                    ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_CANT_PASS, null);
                }
            } else {
                ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
            }
        } else {
//			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
        }
    }

    /**
     * 通知观战者玩家不出牌
     *
     * @param room   房间
     * @param player 不出牌的玩家
     */
    private void notifyWatcherPlayPass(Room room, ClientSide player) {
        for (ClientSide watcher : room.getWatcherList()) {
            ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_PASS, player.getNickname());
        }
    }
}
