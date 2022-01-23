package com.bananna.ninja.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelRefHandler extends ChannelInboundHandlerAdapter {

    private static Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        ctx.fireChannelRegistered();
    }

    public static Channel channel(){
        return channel;
    }
}
