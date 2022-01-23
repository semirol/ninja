package com.bananna.ninja.entity;

import com.bananna.ninja.math.FVector3;
import com.bananna.ninja.message.NinjaOperationTypeEnum;
import lombok.Data;

@Data
public class BattleOperation {

    private int frameNo;

    private int playerId;

    private int operationType;

    private int direction;

    private FVector3 position;

    public static BattleOperation createEmptyOperation(int frameNo, int playerId){
        BattleOperation battleOperation = new BattleOperation();
        battleOperation.setOperationType(NinjaOperationTypeEnum.STAY);
        battleOperation.setFrameNo(frameNo);
        battleOperation.setPlayerId(playerId);
        return battleOperation;
    }

}
