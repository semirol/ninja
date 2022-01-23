package com.bananna.ninja.handler;

import com.bananna.ninja.message.NinjaEnterMessage;
import com.bananna.ninja.message.NinjaRoomMessage;
import com.bananna.ninja.service.PlayerService;
import com.bananna.ninja.service.RoomService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;


@Component
public class NinjaEnterMessageHandler extends SimpleChannelInboundHandler<NinjaEnterMessage> {

    private RoomService roomService;

    private PlayerService playerService;

    public NinjaEnterMessageHandler(RoomService roomService, PlayerService playerService) {
        this.roomService = roomService;
        this.playerService = playerService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NinjaEnterMessage msg) throws Exception {
        int playerId = msg.getPlayerId();
        int ifP1 = roomService.enterRoom(msg.getRoomNumber(), playerId);
        ctx.channel().writeAndFlush(new NinjaRoomMessage(playerId, ifP1));
    }
}
