package com.tuling.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        Channel channel = new Channel();
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new String(byteBuf.array(), StandardCharsets.UTF_8));
    }
}
