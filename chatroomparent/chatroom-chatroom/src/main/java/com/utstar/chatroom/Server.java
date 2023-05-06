package com.utstar.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    private void startServer(int port){
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerEventLootGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.channel(NioServerSocketChannel.class).group(bossEventLoopGroup, workerEventLootGroup).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline().addLast(new HttpServerCodec()).addLast(new ChunkedWriteHandler()).addLast(new HttpObjectAggregator(8192))
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    .addLast(new ConnEstHandler()).addLast(new ServerHandler());
                        }
                    });
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {

            e.printStackTrace();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLootGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server().startServer(6654);
    }
}
