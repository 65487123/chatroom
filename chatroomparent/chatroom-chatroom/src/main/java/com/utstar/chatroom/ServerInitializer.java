package com.utstar.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerInitializer extends ChannelInitializer {


    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new HttpServerCodec()).addLast(new ChunkedWriteHandler()).addLast(new HttpObjectAggregator(8192))
                .addLast(new WebSocketServerProtocolHandler("/hello")).addLast(new ServerHandler());
    }
}
