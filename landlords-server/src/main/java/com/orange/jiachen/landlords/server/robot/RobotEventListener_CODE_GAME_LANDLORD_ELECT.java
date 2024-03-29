package com.orange.jiachen.landlords.server.robot;

import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.entity.Room;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.PokerHelper;
import com.orange.jiachen.landlords.helper.TimeHelper;
import com.orange.jiachen.landlords.robot.RobotDecisionMakers;
import com.orange.jiachen.landlords.server.ServerContains;
import com.orange.jiachen.landlords.server.event.ServerEventListener;

import java.util.ArrayList;
import java.util.List;

public class RobotEventListener_CODE_GAME_LANDLORD_ELECT implements RobotEventListener {

    @Override
    public void call(ClientSide robot, String data) {
        ServerContains.THREAD_EXCUTER.execute(() -> {
            Room room = ServerContains.getRoom(robot.getRoomId());

            List<Poker> landlordPokers = new ArrayList<>(20);
            landlordPokers.addAll(robot.getPokers());
            landlordPokers.addAll(room.getLandlordPokers());

            List<Poker> leftPokers = new ArrayList<>(17);
            leftPokers.addAll(robot.getPre().getPokers());

            List<Poker> rightPokers = new ArrayList<>(17);
            rightPokers.addAll(robot.getNext().getPokers());

            PokerHelper.sortPoker(landlordPokers);
            PokerHelper.sortPoker(leftPokers);
            PokerHelper.sortPoker(rightPokers);

            TimeHelper.sleep(300);

            ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(robot, String.valueOf(RobotDecisionMakers.howToChooseLandlord(room.getDifficultyCoefficient(), leftPokers, rightPokers, landlordPokers)));
        });
    }

}
