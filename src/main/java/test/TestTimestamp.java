package test;
import java.sql.Timestamp;

public class TestTimestamp {
    public static void main(String[] args) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        System.out.println(t);
    }
}
