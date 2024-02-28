package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.entity.Poker;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_WATCH extends ClientEventListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void call(Channel channel, String wrapData) {
        // 退出观战模式后不处理观战请求
        if (!User.INSTANCE.isWatching()) {
            return;
        }

        Map<String, Object> wrapMap = MapHelper.parser(wrapData);
        ClientEventCode rawCode = ClientEventCode.valueOf(wrapMap.get("code").toString());
        Object rawData = wrapMap.get("data");

        switch (rawCode) {
            case CODE_ROOM_JOIN_SUCCESS:
                printJoinPlayerInfo(rawData);
                break;

            // 游戏开始
            case CODE_GAME_STARTING:
                printGameStartInfo(rawData);
                break;

            // 抢地主
            case CODE_GAME_LANDLORD_ELECT:
                printRobLandlord(rawData);
                break;

            // 地主确认
            case CODE_GAME_LANDLORD_CONFIRM:
                printConfirmLandlord(rawData);
                break;

            // 出牌
            case CODE_SHOW_POKERS:
                printPlayPokers(rawData);
                break;

            // 不出（过）
            case CODE_GAME_POKER_PLAY_PASS:
                printPlayPass(rawData);
                break;

            // 玩家退出（此时可以退出观战，修改User.isWatching状态）
            case CODE_CLIENT_EXIT:
                printPlayerExit(rawData, channel);
                break;

            // 玩家被提出房间
            case CODE_CLIENT_KICK:
                printKickInfo(rawData);
                break;

            // 游戏结束（此时可以退出观战，修改User.isWatching状态）
            case CODE_GAME_OVER:
                printGameResult(rawData, channel);
                break;

            // 其他事件忽略
            default:
                break;
        }
    }

    private void printJoinPlayerInfo(Object rawData) {
        printNoticeWithTime("玩家 [" + rawData + "] 加入了房间");
    }

    private void printGameStartInfo(Object rawData) {
        Map<String, Object> map = MapHelper.parser(rawData.toString());

        printNoticeWithTime("游戏开始");
        printNoticeWithTime("玩家1 : " + map.get("player1"));
        printNoticeWithTime("玩家2 : " + map.get("player2"));
        printNoticeWithTime("玩家3 : " + map.get("player3"));
    }

    private void printRobLandlord(Object rawData) {
        printNoticeWithTime("Player [" + rawData + "] didn't choose to become the landlord.");
    }

    private void printConfirmLandlord(Object rawData) {
        Map<String, Object> map = MapHelper.parser(rawData.toString());

        printNoticeWithTime("Player [" + map.get("landlord") + "] has become the landlord and gotten three extra cards:");
        SimplePrinter.printPokers(Noson.convert(map.get("additionalPokers"), new NoType<List<Poker>>() {
        }));
    }

    private void printPlayPokers(Object rawData) {
        Map<String, Object> map = MapHelper.parser(rawData.toString());

        printNoticeWithTime("玩家 [" + map.get("clientNickname") + "] played:");
        SimplePrinter.printPokers(Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        }));
    }

    private void printPlayPass(Object rawData) {
        printNoticeWithTime("玩家 [" + rawData + "] : 跳过");
    }

    private void printPlayerExit(Object rawData, Channel channel) {
        printNoticeWithTime("玩家 [" + rawData + "] 离开了房间");
        quitWatch(channel);
    }

    private void quitWatch(Channel channel) {
        printNoticeWithTime("这个房间将被关闭!");
        printNoticeWithTime("观看结束。再见。");
        SimplePrinter.printNotice("");
        SimplePrinter.printNotice("");

        // 修改玩家是否观战状态
        User.INSTANCE.setWatching(false);

        // 退出watch展示
        get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, "");
    }

    private void printGameResult(Object rawData, Channel channel) {
        Map<String, Object> map = MapHelper.parser(rawData.toString());

        printNoticeWithTime("玩家 [" + map.get("winnerNickname") + "](" + map.get("winnerType") + ") 赢得比赛.");
    }

    private void printKickInfo(Object rawData) {
        printNoticeWithTime("玩家 [" + rawData + "] 因为游手好闲被踢出了。");
    }

    private void printNoticeWithTime(String notice) {
        String msg = FORMATTER.format(LocalDateTime.now()) + "  " + notice;

        SimplePrinter.printNotice(msg);
    }
}
