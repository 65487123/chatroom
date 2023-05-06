package com.utstar.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.*;


public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static List<Channel> channelList = new CopyOnWriteArrayList();
    private ExecutorService executorService = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,new LinkedBlockingDeque<>(),new ThreadFactoryImp("show num"));

    public static int getchannelSize(){
        return channelList.size();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel thisChannel = ctx.channel();
        for (Channel channel : channelList) {
            channel.writeAndFlush(new TextWebSocketFrame(Instant.now().atZone(ZoneId.systemDefault()) + " " + thisChannel.remoteAddress() + "已上线"));
            channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(channelList.size())));
        }
        channelList.add(thisChannel);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel thisChannel = ctx.channel();
        channelList.remove(thisChannel);
        for (Channel channel : channelList) {
            channel.writeAndFlush(new TextWebSocketFrame(Instant.now().atZone(ZoneId.systemDefault()) + " " + thisChannel.remoteAddress() + "已下线"));
            channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(channelList.size())));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame o) {
        for (Channel channel : channelList) {
            channel.writeAndFlush(new TextWebSocketFrame(Instant.now().atZone(ZoneId.systemDefault())+" "+channelHandlerContext.channel().remoteAddress()+":"+o.text()));
        }
    }
}
