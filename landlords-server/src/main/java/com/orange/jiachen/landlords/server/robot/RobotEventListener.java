package com.orange.jiachen.landlords.server.robot;

import com.orange.jiachen.landlords.entity.ClientSide;
import com.orange.jiachen.landlords.enums.ClientEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface RobotEventListener {

    String LISTENER_PREFIX = "com.orange.jiachen.landlords.server.robot.RobotEventListener_";

    Map<ClientEventCode, RobotEventListener> LISTENER_MAP = new HashMap<>();

    void call(ClientSide robot, String data);

    @SuppressWarnings("unchecked")
    static RobotEventListener get(ClientEventCode code) {
        RobotEventListener listener = null;
        try {
            if (RobotEventListener.LISTENER_MAP.containsKey(code)) {
                listener = RobotEventListener.LISTENER_MAP.get(code);
            } else {
                String eventListener = LISTENER_PREFIX + code.name();
                Class<RobotEventListener> listenerClass = (Class<RobotEventListener>) Class.forName(eventListener);
                try {
                    listener = listenerClass.getDeclaredConstructor().newInstance();
                } catch (NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                RobotEventListener.LISTENER_MAP.put(code, listener);
            }
            return listener;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
