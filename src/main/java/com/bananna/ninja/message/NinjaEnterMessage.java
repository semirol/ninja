package com.bananna.ninja.message;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class NinjaEnterMessage extends NinjaMessage{

    public NinjaEnterMessage(int playerId, String roomNumber){
        super(NinjaMessageTypeEnum.ENTER, playerId);
        this.roomNumber = roomNumber;
    }

    private String roomNumber;
}
