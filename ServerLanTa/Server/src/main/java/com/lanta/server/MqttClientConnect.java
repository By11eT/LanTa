package com.lanta.server;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.util.UUID;

public class MqttClientConnect {

    private String connect;
    private IMqttClient client;

    public MqttClientConnect(String connect) {
        this.connect=connect;
    }

    public IMqttClient MqttClientConnect() throws MqttException {
        if (this.connect == "lanta") {
            return lantaConnect();
        }
        else return testConnect();
    }

    public IMqttClient testConnect() throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        this.client = new MqttClient("tcp://localhost:1883",publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
        return client;
    }

    public IMqttClient lantaConnect() throws MqttException {

        String publisherId = UUID.randomUUID().toString();

        this.client = new MqttClient("tcp://iot.lanta.me:1883",publisherId);
        //опции для подключения к серверу
        MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("manager");
            options.setPassword("Zoopooy3ien5".toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

        client.connect(options);

        return client;
    }

    public void close() throws MqttException {
        this.client.disconnect();
    }
}
