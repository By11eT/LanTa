package com.lanta.server;


import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.concurrent.Callable;

public class MqttPublisher implements Callable {
    private static final String TOPIC = "bike/T0001";
    private IMqttClient client;

    public MqttPublisher(IMqttClient client) {
        this.client = client;
    }
    @Override
    public Void call() throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(TOPIC,msg);
        return null;
    }

    private MqttMessage readEngineTemp() {
        double temp =  80;
        byte[] payload = String.format("T:%04.2f",temp)
                .getBytes();
        MqttMessage mqttMessage = new MqttMessage(payload);
        return mqttMessage;
    }
}
