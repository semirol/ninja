package com.bananna.ninja.entity;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Frame {

    private int frameNo;

    private Map<Integer, BattleOperation> operationMap; // key是playerId

    public Frame(int frameNo){
        this.frameNo = frameNo;
        this.operationMap = new ConcurrentHashMap<>();
    }

    public void addOperation(BattleOperation operation){
        if (frameNo == operation.getFrameNo()){
            operationMap.put(operation.getPlayerId(), operation);
        }
    }

    public void addEmptyOperation(int playerId){
        addOperation(BattleOperation.createEmptyOperation(frameNo, playerId));
    }

    public BattleOperation getOperation(int playerId){
        return operationMap.get(playerId);
    }
}
