package com.lanta.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;
    private static MqttPublisher mqttPublisher;

    public MainHandler(MqttPublisher mqttPublisher){
       MainHandler.mqttPublisher =mqttPublisher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент подключился: " + ctx);
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        newClientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if(s.startsWith("{\"a\":")){
            mqttPublisher.call(s);
        }
        else broadcastMessage();
    }

    public void broadcastMessage() {
        String out = parserMqttString.getInformation();
        for (Channel c : channels) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент " + clientName + " вышел из сети");
        channels.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Клиент " + clientName + " отвалился");
        channels.remove(ctx.channel());
        ctx.close();
    }
}