package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.SimpleClient;
import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientEventListener_CODE_GAME_OVER extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        Map<String, Object> map = MapHelper.parser(data);
        SimplePrinter.printNotice("\n玩家 " + map.get("winnerNickname") + "[" + map.get("winnerType") + "]" + " 赢得比赛");

        if (map.containsKey("scores")) {
            List<Map<String, Object>> scores = Noson.convert(map.get("scores"), new NoType<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> score : scores) {
                if (!Objects.equals(score.get("clientId"), SimpleClient.id)) {
                    SimplePrinter.printNotice(score.get("nickName").toString() + "'s rest poker is:");
                    SimplePrinter.printPokers(Noson.convert(score.get("pokers"), new NoType<List<Poker>>() {
                    }));
                }
            }
            SimplePrinter.printNotice("\n");
            // print score
            for (Map<String, Object> score : scores) {
                String scoreInc = score.get("scoreInc").toString();
                String scoreTotal = score.get("score").toString();
                if (SimpleClient.id != (int) score.get("clientId")) {
                    SimplePrinter.printNotice(score.get("nickName").toString() + "的得分是 " + scoreInc + ", 总分是 " + scoreTotal);
                } else {
                    SimplePrinter.printNotice("你的得分是 " + scoreInc + ", 总分是 " + scoreTotal);
                }
            }
            ClientEventListener_CODE_GAME_READY.gameReady(channel);
        }
    }
}
