package com.lanta.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerApp {
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        MqttClientConnect client1 = new MqttClientConnect("test");
        MqttClientConnect client2 = new MqttClientConnect("lanta");

        final MqttPublisher mqttPublisher =  new MqttPublisher(client1.MqttClientConnect());
        final MqttSubscriber mqttSubscriber = new MqttSubscriber(client2.MqttClientConnect());
        mqttSubscriber.call();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new StringDecoder(),
                                    new StringEncoder(),
                                    new MainHandler(mqttPublisher));
                        }
                    });
            ChannelFuture future = b.bind(PORT).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            client1.close();
            client2.close();
        }
    }
}
