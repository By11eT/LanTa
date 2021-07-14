package com.lanta.server;

public class parserMqttString {

    private static String str;
    private static String battery="0";
    private static String geolocation="0,0";
    private static String power;

    public static String getInformation(){
        String s = "Power:"+power+" Battery:"+battery+" Geolocation:"+geolocation;
        return s;
    }
    public static String getGeolocation(){
        return geolocation;
    }

    public static void setStr(String str) {
        parserMqttString.str = str;
        parserStr();
    }

    private static void parserStr(){
        str = str.replaceAll("[{}\"]", "");
        str = str.replaceAll(" ","");
        if(str.startsWith("a")){
            if(str.startsWith("a:27")){
                String msg[]= str.split(",");
                for(String word :msg){
                    if(word.startsWith("b")){
                        battery = word.replaceAll("b:","");
                        System.out.println("Заряд батареи: "+battery);
                    }
                    if(word.startsWith("c")){
                        power = word.replaceAll("c:","");
                        if(power=="1"){
                            power="Самокат выключен";
                        }
                        else power="Самокат включен";
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
