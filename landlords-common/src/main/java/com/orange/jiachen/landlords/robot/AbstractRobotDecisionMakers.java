package com.orange.jiachen.landlords.robot;

import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.entity.PokerSell;

import java.util.List;

/**
 * @author nico
 * @version createTime：2018年11月15日 上午12:12:15
 */

public abstract class AbstractRobotDecisionMakers {

    public abstract PokerSell howToPlayPokers(PokerSell lastPokerSell, ClientSide robot);

    public abstract boolean howToChooseLandlord(List<Poker> leftPokers, List<Poker> rightPokers, List<Poker> myPokers);
}
