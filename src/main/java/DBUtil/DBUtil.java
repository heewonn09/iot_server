package DBUtil;

import java.sql.*;

public class DBUtil {
    // DBMS 초기 연결
    public static Connection getConnect() {
        Connection con = null;
        String DB_ip = "192.168.14.116";
        String DB_port = "3306";
        String DB_name = "smartbuilding";

        String url = "jdbc:mysql://"+DB_ip+":"+DB_port+"/"+DB_name+"?serverTimezone=UTC";
        String user = "sample";
        String password = "1234";
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    // Connection 자원 반납
    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            if(rs!=null)rs.close();
            if(stmt!=null)stmt.close();
            if(con!=null)con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
