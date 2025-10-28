package mqtt;

public interface OnMessageCallback {
    void handle(String topic, String msg);
}
