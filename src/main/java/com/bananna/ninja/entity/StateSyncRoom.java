package com.bananna.ninja.entity;

import com.bananna.ninja.handler.ChannelRefHandler;
import com.bananna.ninja.message.NinjaOperationMessage;
import com.bananna.ninja.message.NinjaOperationTypeEnum;
import com.bananna.ninja.service.RoomService;
import com.bananna.ninja.util.ApplicationContextUtils;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.Executor;

@Data
public class StateSyncRoom implements Room {

    private Executor executor;

    private String roomNumber;

    private int p1Id;

    private int p2Id;

    private int p1Ready;

    private int p2Ready;

    private int p1Send;

    private int p2Send;

    private int maxOfflineCount;

    private int offlineCount;

    public StateSyncRoom(String roomNumber, Executor executor){
        this.executor = executor;
        this.offlineCount = 0;
        this.roomNumber = roomNumber;
        this.maxOfflineCount = 10; // 最大离线秒数
    }

    /**
     * 在收到OperationMessage消息时会被调用
     * @param operation
     */
    public void addBattleOperation(BattleOperation operation){
        NinjaOperationMessage ninjaOperationMessage = null;
        if (Objects.equals(p1Id, operation.getPlayerId())) {
            ninjaOperationMessage = new NinjaOperationMessage(p2Id, operation);
        }
        else if (Objects.equals(p2Id, operation.getPlayerId())) {
            ninjaOperationMessage = new NinjaOperationMessage(p1Id, operation);
        }
        ChannelRefHandler.channel().writeAndFlush(ninjaOperationMessage);
        if (operation.getOperationType() == NinjaOperationTypeEnum.LOSE){
            gcSelf();
        }
    }

    /**
     * 返回是否是p1
     * @param playerId
     * @return
     */
    public int addPlayer(int playerId){
        if (p1Id == 0 || p1Id == playerId) {
            p1Id = playerId;
            System.out.println("p1已进入" + roomNumber + "房间:" + playerId);
            return 1;
        }
        else if (p2Id == 0 && !Objects.equals(playerId, p1Id)) {
            System.out.println("p2已进入" + roomNumber + "房间:" + playerId);
            p2Id = playerId;
            return 0;
        }
        else {
            System.out.println("无法进入房间");
            return 2;
        }
    }

    public void playerReady(int playerId){
        if (Objects.equals(playerId,p1Id)){
            p1Ready = 1;
        }
        else if (Objects.equals(playerId,p2Id)){
            p2Ready = 1;
        }
        if (p1Ready == 1 && p2Ready == 1){
            executor.execute(this::startBattle);
        }
    }

    private void startBattle(){
        while(true){
            if (p1Send == 0 || p2Send == 0){
                offlineCount++;
            }
            else{
                offlineCount = 0;
            }
            if (offlineCount == maxOfflineCount){
                gcSelf();
            }
            p1Send = 0;
            p2Send = 0;
            trySleep();
        }
    }

    private void gcSelf(){
        System.out.println("房间"+roomNumber+"结束对战");
        ApplicationContextUtils.context().getBean(RoomService.class).deleteRoom(roomNumber);
    }

    private void trySleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
