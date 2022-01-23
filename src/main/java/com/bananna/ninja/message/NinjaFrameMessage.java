package com.bananna.ninja.message;

import com.bananna.ninja.entity.Frame;
import lombok.Data;

import java.util.List;

@Data
public class NinjaFrameMessage extends NinjaMessage{

    private List<Frame> frameList;

    public NinjaFrameMessage(int playerId, List<Frame> frameList){
        super(NinjaMessageTypeEnum.FRAME, playerId);
        this.frameList = frameList;
    }

}
