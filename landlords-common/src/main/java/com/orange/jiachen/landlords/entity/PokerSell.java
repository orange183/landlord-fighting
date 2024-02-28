package com.orange.jiachen.landlords.entity;

import com.orange.jiachen.landlords.enums.SellType;
import com.orange.jiachen.landlords.helper.PokerHelper;
import lombok.Data;

import java.util.List;

@Data
public class PokerSell {

    private int score;

    private SellType sellType;

    private List<Poker> sellPokers;

    private int coreLevel;

    public PokerSell(SellType sellType, List<Poker> sellPokers, int coreLevel) {
        this.score = PokerHelper.parseScore(sellType, coreLevel);
        this.sellType = sellType;
        this.sellPokers = sellPokers;
        this.coreLevel = coreLevel;
    }


    @Override
    public String toString() {
        return sellType + "\t| " + score + "\t|" + sellPokers;
    }

}
