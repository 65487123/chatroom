package com.utstar.chatroom;

import com.utstar.chatroom.ServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.Instant;
import java.time.ZoneId;


public class ConnEstHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {




    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame o) {
        Channel channel = channelHandlerContext.channel();
        channel.writeAndFlush(new TextWebSocketFrame(Instant.now().atZone(ZoneId.systemDefault()) + " " + channel.remoteAddress() + "已上线"));
        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(ServerHandler.getchannelSize())));
        channel.pipeline().remove(this);
    }
}

