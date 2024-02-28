package com.orange.jiachen.landlords.entity;

import com.orange.jiachen.landlords.enums.ClientRole;
import com.orange.jiachen.landlords.enums.ClientStatus;
import com.orange.jiachen.landlords.enums.ClientType;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.List;

@Data
public class ClientSide {

    private int id;

    private int roomId;

    private int score;

    private int scoreInc;

    private String nickname;

    private List<Poker> pokers;

    private ClientStatus status;

    private ClientRole role;

    private ClientType type;

    private ClientSide next;

    private ClientSide pre;

    private transient Channel channel;

    private String version;

    public ClientSide() {
    }

    public ClientSide(int id, ClientStatus status, Channel channel) {
        this.id = id;
        this.status = status;
        this.channel = channel;
    }

    public void init() {
        roomId = 0;
        pokers = null;
        status = ClientStatus.TO_CHOOSE;
        type = null;
        next = null;
        pre = null;
        score = 0;
    }


    public final void addScore(int score) {
        this.score += score;
        this.scoreInc = score;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientSide other = (ClientSide) obj;
        return id == other.id;
    }

}
