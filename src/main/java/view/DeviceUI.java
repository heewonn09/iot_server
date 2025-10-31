package view;

import dto.DeviceDTO;

import java.util.List;

public class DeviceUI {
    public void printDevice(List<DeviceDTO> devices){
        int idx = 1;
        for (DeviceDTO d : devices) {
            if (d.getDevice_type().equals("DHT")) {
                System.out.printf("%d. %s (%s) - 온도: %.1f°C, 습도: %.1f%%\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getTemperature(), d.getHumidity());
            } else if (d.getDevice_type().equals("HVAC")) {
                System.out.printf("%d. %s (%s) - 상태: %s 🌀\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getStatus());
            } else {
                System.out.printf("%d. %s (%s) - 상태: %s\n",
                        idx++, d.getDevice_name(), d.getDevice_type(),
                        d.getStatus());
            }
        }
    }
}
