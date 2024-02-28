package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import com.orange.jiachen.landlords.utils.OptionsUtils;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVE extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("人机对战: ");
        SimplePrinter.printNotice("1. 简单模式");
        SimplePrinter.printNotice("2. 中等模式");
        SimplePrinter.printNotice("3. 困难模式");
        SimplePrinter.printNotice("请在上面选择一个选项(输入[back|b]返回选项列表)");
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "pve");

        if (line.equalsIgnoreCase("back") || line.equalsIgnoreCase("b")) {
            get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
            return;
        }

        int choose = OptionsUtils.getOptions(line);

        if (choose < 0 || choose >= 4) {
            SimplePrinter.printNotice("Invalid option, please choose again：");
            call(channel, data);
            return;
        }
        initLastSellInfo();
        pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE_PVE, String.valueOf(choose));
    }
}
