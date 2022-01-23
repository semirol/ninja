package com.bananna.ninja.handler;

import com.bananna.ninja.entity.BattleOperation;
import com.bananna.ninja.entity.Frame;
import com.bananna.ninja.math.FVector3;
import com.bananna.ninja.message.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

public class NinjaMessageEncoder extends MessageToMessageEncoder<NinjaMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NinjaMessage msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        if (msg instanceof NinjaRoomMessage){
            byteBuf.writeIntLE(NinjaMessageTypeEnum.ROOM);
            byteBuf.writeIntLE(((NinjaRoomMessage) msg).getIfP1());
        }
        else if (msg instanceof NinjaFrameMessage){
            byteBuf.writeIntLE(NinjaMessageTypeEnum.FRAME);
            NinjaFrameMessage frameMessage = (NinjaFrameMessage) msg;
            List<Frame> frameList = frameMessage.getFrameList();
            //帧数量
            byteBuf.writeIntLE(frameList.size());
            //每一帧的操作数据
            frameList.forEach(frame -> {
                //帧编号
                byteBuf.writeIntLE(frame.getFrameNo());
                Map<Integer, BattleOperation> operationMap = frame.getOperationMap();
                operationMap.forEach((k,v) -> {
                    byteBuf.writeIntLE(v.getPlayerId());
                    byteBuf.writeIntLE(v.getOperationType());
                    if (v.getOperationType() == NinjaOperationTypeEnum.MOVE){
                        FVector3 position = v.getPosition();
                        byteBuf.writeLongLE(position.getX());
                        byteBuf.writeLongLE(position.getZ());
                    }
                    else{
                        byteBuf.writeIntLE(v.getDirection());
                    }
                });
            });
        }
        DatagramPacket datagramPacket = new DatagramPacket(byteBuf, NinjaMessageDecoder.getAddressByPlayerId(msg.getPlayerId()));
        out.add(datagramPacket);
    }
}
