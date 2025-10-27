package mqtt;

public interface MqttTopicListener {
    void onMessageReceived(String topic, String msg);
}
