package com.bananna.ninja.message;

import com.bananna.ninja.entity.BattleOperation;
import lombok.Data;

@Data
public class NinjaOperationMessage extends NinjaMessage{

    private BattleOperation operation;

    public NinjaOperationMessage(int playerId, BattleOperation operation){
        super(NinjaMessageTypeEnum.ENTER, playerId);
        this.operation = operation;
    }
}
