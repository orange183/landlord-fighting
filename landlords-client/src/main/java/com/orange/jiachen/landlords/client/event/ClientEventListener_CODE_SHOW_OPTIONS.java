package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import com.orange.jiachen.landlords.utils.OptionsUtils;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_SHOW_OPTIONS extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("选项: ");
        SimplePrinter.printNotice("1. 人人对战");
        SimplePrinter.printNotice("2. 人机对战");
        SimplePrinter.printNotice("3. 设置");
        SimplePrinter.printNotice("请选择上面的选项（输入【exit】或【e】退出）");
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "selection");

        if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
            System.exit(0);
        } else {
            int choose = OptionsUtils.getOptions(line);
            if (choose == 1) {
                get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
            } else if (choose == 2) {
                get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
            } else if (choose == 3) {
                get(ClientEventCode.CODE_SHOW_OPTIONS_SETTING).call(channel, data);
            } else {
                SimplePrinter.printNotice("Invalid option, please choose again：");
                call(channel, data);
            }
        }
    }
}
