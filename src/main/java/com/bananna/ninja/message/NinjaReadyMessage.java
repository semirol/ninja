package com.bananna.ninja.message;

import lombok.Data;

@Data
public class NinjaReadyMessage extends NinjaMessage{

    public NinjaReadyMessage(int playerId){
        super(NinjaMessageTypeEnum.READY, playerId);
    }
}
