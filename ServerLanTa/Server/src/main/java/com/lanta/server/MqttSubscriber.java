package com.lanta.server;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MqttSubscriber {
    private static final String TOPIC = "bike";
    private IMqttClient client;

    public MqttSubscriber(IMqttClient client) {
        this.client = client;
    }

    public String call() throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        client.subscribe(TOPIC, (topic, msg) -> {
            byte[] payload = msg.getPayload();
            String str = new String(payload,"UTF-8");
            System.out.println(str);
        });
        return null;
    }
}
