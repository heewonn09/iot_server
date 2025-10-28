package DBUtil;

import java.sql.*;

public class kkmutil {
    
    // ✅ 파일 없이 직접 입력
    private static final String DB_IP = "127.0.0.1";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "kkm";
    private static final String DB_USER = "sample";
    private static final String DB_PASSWORD = "1234";

    public static Connection getConnect() throws SQLException {
        Connection con = null;
        
        try {
            String url = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME + "?serverTimezone=UTC";
            
            System.out.println("🔍 DB 연결 시도: " + url);
            con = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
            System.out.println("✅ DB 연결 성공!");
            
        } catch (SQLException e) {
            System.err.println("❌ DB 연결 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    // Connection 자원 반납
    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}