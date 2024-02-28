package com.orange.jiachen.landlords.entity;

import com.orange.jiachen.landlords.enums.RoomStatus;
import com.orange.jiachen.landlords.enums.RoomType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Data
@NoArgsConstructor
public class Room {

    /**
     * 房间id
     */
    private int id;
    /**
     * 房间归属者
     */
    private String roomOwner;
    /**
     * 房间状态
     */
    private RoomStatus status;
    /**
     * 房间类型
     */
    private RoomType type;

    private Map<Integer, ClientSide> clientSideMap;

    private LinkedList<ClientSide> clientSideList;

    private int landlordId = -1;

    private List<Poker> landlordPokers;

    private PokerSell lastPokerShell;

    private int lastSellClient = -1;

    private int currentSellClient = -1;

    private int difficultyCoefficient;

    private long lastFlushTime;

    private long createTime;

    private int firstSellClient;

    /**
     * 观战者列表
     */
    private List<ClientSide> watcherList = new ArrayList<>(5);

    private int scoreRate = 1;

    private int baseScore = 3;


    public Room(int id) {
        this.id = id;
        this.clientSideMap = new ConcurrentSkipListMap<>();
        this.clientSideList = new LinkedList<>();
        this.status = RoomStatus.WAIT;
        this.createTime = System.currentTimeMillis();
    }

    public int getScore() {
        return this.baseScore * this.scoreRate;
    }


    public void initScoreRate() {
        this.scoreRate = 1;
    }

    public void increaseRate() {
        this.scoreRate *= 2;
    }

    public final long getCreateTime() {
        return createTime;
    }

    public final void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public final int getDifficultyCoefficient() {
        return difficultyCoefficient;
    }

    public final void setDifficultyCoefficient(int difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public final RoomType getType() {
        return type;
    }

    public final void setType(RoomType type) {
        this.type = type;
    }

    public final PokerSell getLastPokerShell() {
        return lastPokerShell;
    }

    public final void setLastPokerShell(PokerSell lastPokerShell) {
        this.lastPokerShell = lastPokerShell;
    }

    public final int getCurrentSellClient() {
        return currentSellClient;
    }

    public final void setCurrentSellClient(int currentSellClient) {
        this.currentSellClient = currentSellClient;
    }


    public final String getRoomOwner() {
        return roomOwner;
    }

    public final void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final RoomStatus getStatus() {
        return status;
    }

    public final void setStatus(RoomStatus status) {
        this.status = status;
    }

    public final Map<Integer, ClientSide> getClientSideMap() {
        return clientSideMap;
    }

    public final void setClientSideMap(Map<Integer, ClientSide> clientSideMap) {
        this.clientSideMap = clientSideMap;
    }

}
