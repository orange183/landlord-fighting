package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.helper.PokerHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import com.orange.jiachen.landlords.utils.OptionsUtils;
import io.netty.channel.Channel;

public class ClientEventListener_CODE_SHOW_OPTIONS_SETTING extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        SimplePrinter.printNotice("设置: ");
        SimplePrinter.printNotice("1. 带有形状边的卡片(默认)");
        SimplePrinter.printNotice("2. 圆角卡");
        SimplePrinter.printNotice("3. 有类型的纯文本");
        SimplePrinter.printNotice("4. 没有类型的纯文本");
        SimplePrinter.printNotice("5. Unicode的卡片");

        SimplePrinter.printNotice("请在上面选择一个选项(输入[BACK]返回选项列表)");
        String line = SimpleWriter.write(User.INSTANCE.getNickname(), "setting");

        if (line.equalsIgnoreCase("BACK")) {
            get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
        } else {
            int choose = OptionsUtils.getOptions(line);

            if (choose >= 1 && choose <= 5) {
                PokerHelper.pokerPrinterType = choose - 1;
                get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
            } else {
                SimplePrinter.printNotice("无效设置，请重新选择:");
                call(channel, data);
            }
        }
    }


}
