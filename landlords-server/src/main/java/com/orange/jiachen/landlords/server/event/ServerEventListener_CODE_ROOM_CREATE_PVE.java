package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.channel.ChannelUtils;
import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.*;
import com.orange.jiachen.landlords.robot.RobotDecisionMakers;
import com.orange.jiachen.landlords.server.ServerContains;

public class ServerEventListener_CODE_ROOM_CREATE_PVE implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {

        int difficultyCoefficient = Integer.parseInt(data);
        if (!RobotDecisionMakers.contains(difficultyCoefficient)) {
            ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_PVE_DIFFICULTY_NOT_SUPPORT, null);
            return;
        }

        Room room = new Room(ServerContains.getServerId());
        room.setType(RoomType.PVE);
        room.setStatus(RoomStatus.BLANK);
        room.setRoomOwner(clientSide.getNickname());
        room.getClientSideMap().put(clientSide.getId(), clientSide);
        room.getClientSideList().add(clientSide);
        room.setCurrentSellClient(clientSide.getId());
        room.setCreateTime(System.currentTimeMillis());
        room.setDifficultyCoefficient(difficultyCoefficient);

        clientSide.setRoomId(room.getId());
        ServerContains.addRoom(room);

        ClientSide preClient = clientSide;
        // Add robots
        for (int index = 1; index < 3; index++) {
            ClientSide robot = new ClientSide(-ServerContains.getClientId(), ClientStatus.PLAYING, null);
            robot.setNickname("机器人\uD83E\uDD16_" + index);
            robot.setRole(ClientRole.ROBOT);
            preClient.setNext(robot);
            robot.setPre(preClient);
            robot.setRoomId(room.getId());
            room.getClientSideMap().put(robot.getId(), robot);
            room.getClientSideList().add(robot);

            preClient = robot;
            ServerContains.CLIENT_SIDE_MAP.put(robot.getId(), robot);
        }
        preClient.setNext(clientSide);
        clientSide.setPre(preClient);

        ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, String.valueOf(room.getId()));
    }


}
