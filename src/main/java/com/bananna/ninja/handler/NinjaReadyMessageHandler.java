package com.bananna.ninja.handler;

import com.bananna.ninja.entity.Player;
import com.bananna.ninja.entity.Room;
import com.bananna.ninja.message.NinjaReadyMessage;
import com.bananna.ninja.service.PlayerService;
import com.bananna.ninja.service.RoomService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

@Component
public class NinjaReadyMessageHandler extends SimpleChannelInboundHandler<NinjaReadyMessage> {

    private RoomService roomService;

    private PlayerService playerService;

    public NinjaReadyMessageHandler(RoomService roomService, PlayerService playerService) {
        this.roomService = roomService;
        this.playerService = playerService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NinjaReadyMessage msg) throws Exception {
        int playerId = msg.getPlayerId();
        Room room = roomService.getRoomByPlayerId(playerId);
        room.playerReady(playerId);
    }
}
