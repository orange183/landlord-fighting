package com.orange.jiachen.landlords.client.event;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.enums.ClientEventCode;
import com.orange.jiachen.landlords.enums.ServerEventCode;
import com.orange.jiachen.landlords.helper.MapHelper;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;
import io.netty.channel.Channel;
import org.nico.noson.util.string.StringUtils;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_NICKNAME_SET extends ClientEventListener {

    public static final int NICKNAME_MAX_LENGTH = 10;

    @Override
    public void call(Channel channel, String data) {

        // If it is not the first time that the user is prompted to enter nickname
        // If first time, data = null or "" otherwise not empty
        if (StringUtils.isNotBlank(data)) {
            Map<String, Object> dataMap = MapHelper.parser(data);
            if (dataMap.containsKey("invalidLength")) {
                SimplePrinter.printNotice("Your nickname has invalid length: " + dataMap.get("invalidLength"));
            }
        }
        SimplePrinter.printNotice("请设置你的昵称 (最大 " + NICKNAME_MAX_LENGTH + " 字符)");
        String nickname = SimpleWriter.write(User.INSTANCE.getNickname(), "nickname");

        // If the length of nickname is more that NICKNAME_MAX_LENGTH
        if (nickname.trim().length() > NICKNAME_MAX_LENGTH) {
            String result = MapHelper.newInstance().put("invalidLength", nickname.trim().length()).json();
            get(ClientEventCode.CODE_CLIENT_NICKNAME_SET).call(channel, result);
        } else {
            pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, nickname);
            User.INSTANCE.setNickname(nickname);
        }
    }


}
