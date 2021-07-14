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

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//создание потока для принятия запросов на подключение
        EventLoopGroup workerGroup = new NioEventLoopGroup();//создание потоков для работы с клиентами

        //MqttClientConnect client1 = new MqttClientConnect("test");//создание клиентов mqtt для publisher или subscribe
        MqttClientConnect client2 = new MqttClientConnect("lanta");

       // final MqttPublisher mqttPublisher =  new MqttPublisher(client1.MqttClientConnect());//создание publisher
        final MqttSubscriber mqttSubscriber = new MqttSubscriber(client2.MqttClientConnect());//создание subscribe
        mqttSubscriber.call();//вызов прослушки mqtt-брокера для того,чтобы сервер получал message

        try {
            ServerBootstrap b = new ServerBootstrap();//предварительная настройка сервера(создание объекта сервер)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(//Handler'ы для pipeline , основа принятия и отправки сообщений между серверо и клиентом
                                    new StringDecoder(),
                                    new StringEncoder(),
                                    new MainHandler(null));//наш handler
                        }
                    });
            ChannelFuture future = b.bind(PORT).sync();//Запуск сервера

            future.channel().closeFuture().sync();//сервер будет работать до тех пор ,пока не сработает эта строка.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//Закрытие всех потоков.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            //client1.close();
            client2.close();
        }
    }
}
