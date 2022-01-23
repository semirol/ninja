package com.bananna.ninja.message;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class NinjaRoomMessage extends NinjaMessage{
    private int ifP1;

    public NinjaRoomMessage(int playerId, int ifP1){
        super(NinjaMessageTypeEnum.ROOM, playerId);
        this.ifP1 = ifP1;
    }
}
