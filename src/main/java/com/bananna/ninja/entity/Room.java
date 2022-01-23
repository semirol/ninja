package com.bananna.ninja.entity;

import com.bananna.ninja.handler.ChannelRefHandler;
import com.bananna.ninja.message.NinjaFrameMessage;
import com.bananna.ninja.message.NinjaOperationTypeEnum;
import com.bananna.ninja.service.RoomService;
import com.bananna.ninja.util.ApplicationContextUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

@Data
public class Room {

    private Executor executor;

    private String roomNumber;

    private int p1Id;

    private int p2Id;

    private int p1Ready;

    private int p2Ready;

    private volatile int p1FrameNo;

    private volatile int p2FrameNo;

    private boolean ifBattleEnd;

    private int frameInterval;

    private int maxOfflineCount;

    private int offlineCount;

    private int frameNo;

    private Frame thisFrame;

    private List<Frame> allFrames;

    public Room(String roomNumber, Executor executor){
        this.roomNumber = roomNumber;
        this.executor = executor;
        this.frameNo = 0;
        this.thisFrame = new Frame(frameNo);
        this.allFrames = new ArrayList<>();
        this.frameInterval = 66; // 默认66ms一个逻辑帧
        this.maxOfflineCount = 150; // 最大离线帧数，约10秒
    }

    /**
     * 在收到BattleMessage消息时会被调用
     * @param operation
     */
    public void addBattleOperation(BattleOperation operation){
        if (frameNo == operation.getFrameNo()){
            thisFrame.addOperation(operation);
            if (Objects.equals(p1Id, operation.getPlayerId())) {
                p1FrameNo = frameNo;
            }
            else if (Objects.equals(p2Id, operation.getPlayerId())) {
                p2FrameNo = frameNo;
            }
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

    public int getEnemyPlayerId(int playerId){
        if (Objects.equals(p1Id,p2Id)) {
            return p2Id;
        } else {
            return p1Id;
        }
    }

    /**
     * 主循环逻辑
     * 开始对战，开始第一帧逻辑帧
     */
    private void startBattle(){
        System.out.println("房间" + roomNumber + "开始对局");
        while (!ifBattleEnd){
            System.out.println("发送第"+frameNo+"帧");
            fillMissingOperations();
            copyAndResetThisFrames(); // 这里自增了frameNo
            sendOpsToAll();
            trySleepFrameInterval();
        }
        System.out.println("房间" + roomNumber + "对局结束");
        //清除自身释放内存
        ApplicationContextUtils.context().getBean(RoomService.class).deleteRoom(roomNumber);
    }

    /**
     * 填充当前帧中缺失的操作信息
     */
    private void fillMissingOperations(){
        boolean flag = false;
        if (thisFrame.getOperation(p1Id) == null){
            flag = true;
            System.out.println("p1丢帧");

            thisFrame.addEmptyOperation(p1Id);
        }
        if (thisFrame.getOperation(p2Id) == null){
            flag = true;
            System.out.println("p2丢帧");

            thisFrame.addEmptyOperation(p2Id);
        }
        //记录一次掉线，记录数量达到
        if (flag == true){
            offlineCount++;
            if (offlineCount > maxOfflineCount){
                // 关闭房间
                ifBattleEnd = true;
            }
        }
        else{
            offlineCount = 0;
        }
    }

    /**
     * 休眠一个逻辑帧长度
     */
    private void trySleepFrameInterval(){
        try {
            Thread.sleep(frameInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将该段时间内（一帧）的所有操作广播给每个玩家
     */
    private void sendOpsToAll(){
        List<Frame> p1SendFrames = allFrames.subList(p1FrameNo, allFrames.size());
        NinjaFrameMessage p1Message = new NinjaFrameMessage(p1Id, p1SendFrames);
        ChannelRefHandler.channel().writeAndFlush(p1Message);

        List<Frame> p2SendFrames = allFrames.subList(p2FrameNo, allFrames.size());
        NinjaFrameMessage p2Message = new NinjaFrameMessage(p2Id, p2SendFrames);
        ChannelRefHandler.channel().writeAndFlush(p2Message);
    }

    /**
     * 将当前帧信息保存在历史帧信息中，并清空当前帧
     */
    private synchronized void copyAndResetThisFrames(){
        allFrames.add(thisFrame);
        frameNo++;
        thisFrame = new Frame(frameNo);
    }

}
