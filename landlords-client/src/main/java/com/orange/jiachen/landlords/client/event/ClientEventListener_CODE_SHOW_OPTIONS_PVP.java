package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import com.orange.jiachen.landlords.utils.OptionsUtils;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVP extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("人人对战: ");
        SimplePrinter.printNotice("1. 创建房间");
        SimplePrinter.printNotice("2. 房间列表");
        SimplePrinter.printNotice("3. 加入房间");
        SimplePrinter.printNotice("4. 出席观看比赛");
        SimplePrinter.printNotice("请在上面选择一个选项(输入[back|b]返回选项列表)");
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "pvp");
        if (line == null) {
            SimplePrinter.printNotice("无效选项，请重新选择:");
            call(channel, data);
            return;
        }

        if (line.equalsIgnoreCase("BACK") || line.equalsIgnoreCase("b")) {
            get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
            return;
        }

        int choose = OptionsUtils.getOptions(line);
        switch (choose) {
            case 1:
                pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
                break;
            case 2:
                pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);
                break;
            case 3:
                handleJoinRoom(channel, data);
                break;
            case 4:
                handleJoinRoom(channel, data, true);
                break;
            default:
                SimplePrinter.printNotice("无效选项，请重新选择:");
                call(channel, data);
        }
    }

    private void parseInvalid(Channel channel, String data) {
        SimplePrinter.printNotice("无效选项，请重新选择:");
        call(channel, data);
    }

    private void handleJoinRoom(Channel channel, String data) {
        handleJoinRoom(channel, data, false);
    }

    private void handleJoinRoom(Channel channel, String data, Boolean watchMode) {
        String notice = String.format("请输入您想要 %s 的房间id (输入[back|b]返回选项列表)", watchMode ? "观看" : "加入");

        SimplePrinter.printNotice(notice);
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "roomid");
        if (line == null) {
            parseInvalid(channel, data);
            return;
        }

        if (line.equalsIgnoreCase("BACK") || line.equalsIgnoreCase("b")) {
            call(channel, data);
            return;
        }

        int option = OptionsUtils.getOptions(line);
        if (option < 1) {
            parseInvalid(channel, data);
            return;
        }

        pushToServer(channel, watchMode ? ServerEventCode.CODE_GAME_WATCH : ServerEventCode.CODE_ROOM_JOIN, String.valueOf(option));
    }
}
