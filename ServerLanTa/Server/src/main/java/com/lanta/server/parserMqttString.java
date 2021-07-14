package com.lanta.server;

public class parserMqttString {

    private static String str;
    private static String battery;
    private static String geolocation;

    public static String getBattery() {
        return battery;
    }
    public static String getGeolocation(){
        return geolocation;
    }
    public static void setStr(String str) {
        parserMqttString.str = str;
        parserStr();
    }

    private static void parserStr(){
        System.out.println(str);
        str = str.replaceAll("[{}\"]", "");
        System.out.println(str);
        str = str.replaceAll(" ","");
        System.out.println(str);
        if(str.startsWith("a")){
            if(str.startsWith("a:27")){
                String msg[]= str.split(",");
                for(String word :msg){
                    if(word.startsWith("b")){
                        battery = word.replaceAll("b:","");
                        System.out.println("Заряд батареи: "+battery);
                    }
                }
            }
            if(str.startsWith("a:19")){
                String[] msg = str.split(",");
                for(int i=0;i< msg.length;i++) {
                    if(msg[i].startsWith("g:")){
                        msg[i]=msg[i].replaceAll("g:","");
                        geolocation = msg[i]+","+msg[i+1];
                        System.out.println("Геолокация: "+ geolocation);
                        break;
                    }
                }
            }
        }
    }
}
