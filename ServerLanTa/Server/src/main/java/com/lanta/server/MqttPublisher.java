package com.lanta.server;


import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher  {
    private static final String TOPIC = "bike";
    private IMqttClient client;

    public MqttPublisher(IMqttClient client) {
        this.client = client;
    }

    public Void call(String s) throws Exception {
        if ( !client.isConnected()) {
            return null;
        }
        MqttMessage msg = readEngine(s);
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(TOPIC,msg);

        return null;
    }

    private MqttMessage readEngine(String s) {

        byte[] payload = s.getBytes();
        MqttMessage mqttMessage = new MqttMessage(payload);
        return mqttMessage;

    }
}
