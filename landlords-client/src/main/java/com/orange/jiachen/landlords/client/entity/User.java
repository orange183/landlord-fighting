package com.orange.jiachen.landlords.client.entity;

import lombok.Data;

@Data
public class User {
    public static final User INSTANCE = new User();

    /**
     * 是否游戏中
     */
    private volatile boolean isPlaying = false;

    /**
     * 是否观战中
     */
    private volatile boolean isWatching = false;

    /**
     * 用户昵称
     */
    private String nickname = "玩家";

}
