package util;

import java.util.ArrayList;

// 사용할 iot 디바이스 타입 목록들을 선언하는 클래스
public class DeviceTypeList {
    private static ArrayList<String> devices;
    static {
        devices = new ArrayList<>();
        devices.add("elevator");
        devices.add("RFID");
        devices.add("MQ2");
        devices.add("DHT");
        devices.add("LED");
    }
    public static ArrayList<String> getDevices(){
        return devices;
    }
}
