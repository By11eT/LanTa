package com.lanta.server;

import org.eclipse.paho.client.mqttv3.IMqttClient;

public class MqttSubscriber {

    private static final String TOPIC = "bike";
    private IMqttClient client;
    private String str;

    public MqttSubscriber(IMqttClient client) {
        this.client = client;
    }

    public String call() throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        client.subscribe(TOPIC, (topic, msg) -> {
            byte[] payload = msg.getPayload();
            this.str = new String(payload,"UTF-8");
            System.out.println(str);
        });

        return null;
    }
}
