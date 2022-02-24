package com.bananna.ninja.handler;

import com.bananna.ninja.entity.BattleOperation;
import com.bananna.ninja.math.FVector3;
import com.bananna.ninja.message.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NinjaMessageDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static Map<Integer, InetSocketAddress> playerAddressMap = new ConcurrentHashMap<>();

    public static InetSocketAddress getAddressByPlayerId(int playerId){
        return playerAddressMap.get(playerId);
    }
    /**
     * 消息格式：
     * ｜消息类型（2字节）｜...｜
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content();
        //读取消息类型
        int messageType = byteBuf.readIntLE();
        int playerId = byteBuf.readIntLE();
        playerAddressMap.put(playerId, msg.sender()); // todo 不用每次都更新map，太耗时
        if (messageType == NinjaMessageTypeEnum.ENTER){
            int length = byteBuf.readByte();
            String roomNumber = byteBuf.readCharSequence(length, CharsetUtil.UTF_8).toString();
            NinjaEnterMessage ninjaEnterMessage = new NinjaEnterMessage(playerId, roomNumber);
            out.add(ninjaEnterMessage);
        }
        else if (messageType == NinjaMessageTypeEnum.READY){
            out.add(new NinjaReadyMessage(playerId));
        }
        else if (messageType == NinjaMessageTypeEnum.OPERATION){
            BattleOperation battleOperation = new BattleOperation();
            battleOperation.setPlayerId(playerId);
            //读取帧号
            int frameNo = byteBuf.readIntLE();
            battleOperation.setFrameNo(frameNo);
            //读取操作类型
            int operationType = byteBuf.readIntLE();
            battleOperation.setOperationType(operationType);
            if (operationType == NinjaOperationTypeEnum.MOVE){
                //读取位置
                long x = byteBuf.readLongLE();
                long z = byteBuf.readLongLE();
                battleOperation.setPosition(new FVector3(x, 0, z));
            }
            //读取方向
            int direction = byteBuf.readIntLE();
            battleOperation.setDirection(direction);
            NinjaOperationMessage message = new NinjaOperationMessage(playerId, battleOperation);
            out.add(message);
        }
    }
}
