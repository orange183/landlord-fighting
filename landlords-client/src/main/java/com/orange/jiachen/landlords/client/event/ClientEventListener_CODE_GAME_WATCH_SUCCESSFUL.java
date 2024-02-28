package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import io.netty.channel.Channel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ClientEventListener_CODE_GAME_WATCH_SUCCESSFUL extends ClientEventListener {

    private static final String WATCH_SUCCESSFUL_TIPS = "                                                 \n" +
            "+------------------------------------------------\n" +
            "|你已经在看比赛了。      \n" +
            "|房间主人: %s. 房间当前状态: %s.\n" +
            "+------------------------------------------------\n" +
            "(提示:输入[exit|e]退出。)                  \n" +
            "                                                   ";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void call(Channel channel, String data) {
        // 修改User.isWatching状态
        // Edit User.isWatching
        User.INSTANCE.setWatching(true);

        // 进入观战提示
        // Enter spectator mode
        Map<String, Object> map = MapHelper.parser(data);
        SimplePrinter.printNotice(String.format(WATCH_SUCCESSFUL_TIPS, map.get("owner"), map.get("status")));

        // 监听输入用于退出
        // Listen enter event to exit spectator mode
        new Thread(() -> registerExitEvent(channel), "exit-spectator-input-thread").start();
    }

    private void registerExitEvent(Channel channel) {
        String enter = SimpleWriter.write();
        if ("exit".equalsIgnoreCase(enter) || "e".equalsIgnoreCase(enter)) {
            quitWatch(channel);
            return;
        }

        printCommandUsage();
        registerExitEvent(channel);
    }

    private void quitWatch(Channel channel) {
        SimplePrinter.printNotice(FORMATTER.format(LocalDateTime.now()) + "  观看结束。再见。");
        SimplePrinter.printNotice("");
        SimplePrinter.printNotice("");

        // 修改玩家是否观战状态
        User.INSTANCE.setWatching(false);

        // 退出观战模式
        pushToServer(channel, ServerEventCode.CODE_GAME_WATCH_EXIT);
        get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, "");
    }

    private void printCommandUsage() {
        SimplePrinter.printNotice("输入[exit|e]退出");
    }
}
