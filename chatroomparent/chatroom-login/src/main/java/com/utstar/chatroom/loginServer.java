package com.utstar.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author：luzeping
 * @Date: 2020/1/17 17:16
 */
@SpringBootApplication
public class loginServer {
    /**
     * description:
     * @Author：luzeping
    
     * @return void
     */
    private void startServer(int port){
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerEventLootGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.channel(NioServerSocketChannel.class).group(bossEventLoopGroup, workerEventLootGroup).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ServerInitializer());
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {

            e.printStackTrace();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLootGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {

    }
}
