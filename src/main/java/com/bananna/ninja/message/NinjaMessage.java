package com.bananna.ninja.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NinjaMessage {

    public NinjaMessage(int messageType, int playerId) {
        this.messageType = messageType;
        this.playerId = playerId;
    }

    private int messageType;

    private int playerId;

}
