package com.bananna.ninja.handler;

import com.bananna.ninja.entity.BattleOperation;
import com.bananna.ninja.entity.Player;
import com.bananna.ninja.entity.Room;
import com.bananna.ninja.message.NinjaOperationMessage;
import com.bananna.ninja.service.PlayerService;
import com.bananna.ninja.service.RoomService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
public class NinjaOperationMessageHandler extends SimpleChannelInboundHandler<NinjaOperationMessage> {

    private RoomService roomService;

    private PlayerService playerService;

    public NinjaOperationMessageHandler(RoomService roomService, PlayerService playerService) {
        this.roomService = roomService;
        this.playerService = playerService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NinjaOperationMessage msg) throws Exception {
        Room room = roomService.getRoomByPlayerId(msg.getPlayerId());
        room.addBattleOperation(msg.getOperation());
    }
}
