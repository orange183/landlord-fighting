package com.orange.jiachen.landlords.server.timer;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.*;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.server.ServerContains;
import com.orange.jiachen.landlords.server.event.ServerEventListener;
import com.orange.jiachen.landlords.server.robot.RobotEventListener;

import java.util.Map;
import java.util.TimerTask;

/**
 * @author nico
 */

public class RoomClearTask extends TimerTask {

    /**
     * 创建后的房间等待时间为100秒
     */
    private static final long waitingStatusInterval = 1000 * 100;

    /**
     * 房间开始破坏的时间是100秒
     */
    private static final long startingStatusInterval = 1000 * 300;

    /**
     * 房间寿命是600秒
     */
    private static final long liveTime = 1000 * 60 * 20;

    @Override
    public void run() {
        try {
            doing();
        } catch (Exception e) {
            SimplePrinter.serverLog(e.getMessage());
        }

    }

    public void doing() {
        Map<Integer, Room> rooms = ServerContains.getRoomMap();
        if (rooms == null || rooms.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();
        for (Room room : rooms.values()) {
            long alreadyLiveTime = System.currentTimeMillis() - room.getCreateTime();
            SimplePrinter.serverLog("房间 " + room.getId() + " 已经活跃 " + alreadyLiveTime + "ms");
            if (alreadyLiveTime > liveTime) {
                SimplePrinter.serverLog("房间 " + room.getId() + " live time overflow " + liveTime + ", 关闭!");
                ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(room.getClientSideList().get(0), null);
                continue;
            }

            long diff = now - room.getLastFlushTime();
            if (room.getStatus() != RoomStatus.STARTING && diff > waitingStatusInterval) {
                SimplePrinter.serverLog("房间 " + room.getId() + " 启动等待时间溢出 " + waitingStatusInterval + ", 关闭!");
                ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(room.getClientSideList().get(0), null);
                continue;
            }
            if (room.getType() != RoomType.PVP) {
                continue;
            }

            if (diff <= startingStatusInterval) {
                continue;
            }

            boolean allRobots = true;
            for (ClientSide client : room.getClientSideList()) {
                if (client.getId() != room.getCurrentSellClient() && client.getRole() == ClientRole.PLAYER) {
                    allRobots = false;
                    break;
                }
            }

            ClientSide currentPlayer = room.getClientSideMap().get(room.getCurrentSellClient());

            if (allRobots) {
                SimplePrinter.serverLog("房间 " + room.getId() + " 都是机器人，关闭!");
                ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(currentPlayer, null);
                continue;
            }
            // 踢掉这个客户端
            ChannelUtils.pushToClient(currentPlayer.getChannel(), ClientEventCode.CODE_CLIENT_KICK, null);

            notifyWatcherClientKick(room, currentPlayer);

            // 客户端当前玩家
            room.getClientSideMap().remove(currentPlayer.getId());
            room.getClientSideList().remove(currentPlayer);

            ClientSide robot = new ClientSide(-ServerContains.getClientId(), ClientStatus.PLAYING, null);
            robot.setNickname(currentPlayer.getNickname());
            robot.setRole(ClientRole.ROBOT);
            robot.setRoomId(room.getId());
            robot.setNext(currentPlayer.getNext());
            robot.setPre(currentPlayer.getPre());
            robot.getNext().setPre(robot);
            robot.getPre().setNext(robot);
            robot.setPokers(currentPlayer.getPokers());
            robot.setType(currentPlayer.getType());

            room.getClientSideMap().put(robot.getId(), robot);
            room.getClientSideList().add(robot);
            room.setCurrentSellClient(robot.getId());

            // If last sell client is current client, replace it to robot id
            if (room.getLastSellClient() == currentPlayer.getId()) {
                room.setLastSellClient(robot.getId());
            }

            // set robot difficulty -> simple
            room.setDifficultyCoefficient(1);

            ServerContains.CLIENT_SIDE_MAP.put(robot.getId(), robot);

            // 初始化客户端
            currentPlayer.init();

            SimplePrinter.serverLog("房间 " + room.getId() + " 玩家 " + currentPlayer.getNickname() + " " + startingStatusInterval + "ms不操作，自动保管!");

            RobotEventListener.get(room.getLandlordId() == -1 ? ClientEventCode.CODE_GAME_LANDLORD_ELECT : ClientEventCode.CODE_GAME_POKER_PLAY).call(robot, null);
        }
    }

    /**
     * 通知观战者玩家被提出房间
     *
     * @param room   房间
     * @param player 被提出的玩家
     */
    private void notifyWatcherClientKick(Room room, ClientSide player) {
        for (ClientSide watcher : room.getWatcherList()) {
            ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_CLIENT_KICK, player.getNickname());
        }
    }
}
