package com.orange.jiachen.landlords.server.event;

import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.enums.ServerEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface ServerEventListener {

    void call(ClientSide client, String data);

    Map<ServerEventCode, ServerEventListener> LISTENER_MAP = new HashMap<>();

    String LISTENER_PREFIX = "com.orange.jiachen.landlords.server.event.ServerEventListener_";

    @SuppressWarnings("unchecked")
    static ServerEventListener get(ServerEventCode code) {
        ServerEventListener listener = null;
        try {
            if (ServerEventListener.LISTENER_MAP.containsKey(code)) {
                listener = ServerEventListener.LISTENER_MAP.get(code);
            } else {
                String eventListener = LISTENER_PREFIX + code.name();
                Class<ServerEventListener> listenerClass = (Class<ServerEventListener>) Class.forName(eventListener);
                try {
                    listener = listenerClass.getDeclaredConstructor().newInstance();
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                ServerEventListener.LISTENER_MAP.put(code, listener);
            }
            return listener;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
