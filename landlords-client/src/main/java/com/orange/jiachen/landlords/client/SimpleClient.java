package com.orange.jiachen.landlords.client;

import com.orange.jiachen.landlords.client.entity.User;
import com.orange.jiachen.landlords.client.proxy.ProtobufProxy;
import com.orange.jiachen.landlords.client.proxy.WebsocketProxy;
import com.orange.jiachen.landlords.features.Features;
import com.orange.jiachen.landlords.print.SimplePrinter;
import com.orange.jiachen.landlords.print.SimpleWriter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient {

    public final static String VERSION = Features.VERSION_1_3_0;
    private final static String[] serverAddressSource = new String[]{
            "https://raw.githubusercontent.com/ainilili/ratel/master/serverlist.json",            // Source
            "https://cdn.jsdelivr.net/gh/ainilili/ratel@master/serverlist.json",                // CN CDN
            "https://raw.fastgit.org/ainilili/ratel/master/serverlist.json",                // HongKong CDN
            "https://cdn.staticaly.com/gh/ainilili/ratel/master/serverlist.json",                // Japanese CDN
            "https://ghproxy.com/https://raw.githubusercontent.com/ainilili/ratel/master/serverlist.json",    // Korea CDN
            "https://gitee.com/ainilili/ratel/raw/master/serverlist.json"                    // CN Gitee
    };
    public static int id = -1;
    /**
     * 服务器ip地址
     */
    public static String serverAddress;
    /**
     * 服务器端口号
     */
    public static int port = 1024;
    /**
     * 客户端与服务端的通信协议
     */
    public static String protocol = "pb";

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        // 通过java -jar landlords-client-${version} -p 1024 -h 127.0.0.1 -ptl pb 手动指定服务器相关信息
        if (args != null && args.length > 0) {
            for (int index = 0; index < args.length; index = index + 2) {
                if (index + 1 < args.length) {
                    if (args[index].equalsIgnoreCase("-p") || args[index].equalsIgnoreCase("-port")) {
                        port = Integer.parseInt(args[index + 1]);
                    }
                    if (args[index].equalsIgnoreCase("-h") || args[index].equalsIgnoreCase("-host")) {
                        serverAddress = args[index + 1];
                    }
                    if (args[index].equalsIgnoreCase("-ptl") || args[index].equalsIgnoreCase("-protocol")) {
                        protocol = args[index + 1];
                    }
                }
            }
        }
        // 没有指定的话读取配置文件获取服务器信息
        if (serverAddress == null) {
            List<String> serverAddressList = getServerAddressList();
            if (serverAddressList.isEmpty()) {
                throw new RuntimeException("请使用'-host'去设置服务地址。");
            }

            SimplePrinter.printNotice("请选择一个服务:");
            for (int i = 0; i < serverAddressList.size(); i++) {
                SimplePrinter.printNotice((i + 1) + ". " + serverAddressList.get(i));
            }
            int serverPick = Integer.parseInt(SimpleWriter.write(User.INSTANCE.getNickname(), "option"));
            while (serverPick < 1 || serverPick > serverAddressList.size()) {
                try {
                    SimplePrinter.printNotice("服务器地址不存在!");
                    serverPick = Integer.parseInt(SimpleWriter.write(User.INSTANCE.getNickname(), "option"));
                } catch (NumberFormatException ignore) {
                }
            }
            serverAddress = serverAddressList.get(serverPick - 1);
            String[] elements = serverAddress.split(":");
            serverAddress = elements[0];
            port = Integer.parseInt(elements[1]);
        }

        if (protocol.equals("pb")) {
            new ProtobufProxy().connect(serverAddress, port);
        } else if (protocol.equals("ws")) {
            new WebsocketProxy().connect(serverAddress, port + 1);
        } else {
            throw new UnsupportedOperationException("不支持的协议：" + protocol);
        }
    }

    private static List<String> getServerAddressList() {
        List<String> serverAddressList = new ArrayList<>();
        serverAddressList.add("127.0.0.1:1024:本地protobuf服务器[v1.4.0]");
        // for (String serverAddressSource : serverAddressSource) {
        //     try {
        //         String serverInfo = StreamUtils.convertToString(new URL(serverAddressSource));
        //         serverAddressList.addAll(Noson.convert(serverInfo, new NoType<List<String>>() {
        //         }));
        //     } catch (IOException e) {
        //         SimplePrinter.printNotice("连接 " + serverAddressSource + " 失败: " + e.getMessage());
        //     }
        // }
        return serverAddressList;
    }

}
