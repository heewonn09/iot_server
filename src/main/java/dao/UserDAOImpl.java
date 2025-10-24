package dao;

import DBUtil.DBUtil;
import dto.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO{
    @Override
    public MemberDTO login(String id, String pw) {
        String sql = """
                select * from users
                where id = ? and pass = ?
                """;
        Connection con = DBUtil.getConnect();
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        MemberDTO user = null;
        try {
            ptmt = con.prepareStatement(sql);
            ptmt.setString(1,id);
            ptmt.setString(2,pw);

            rs = ptmt.executeQuery();
            if(rs.next()){
                user = new MemberDTO(rs.getInt("user_id"), rs.getString("id"),rs.getString("name"), rs.getString("card_id"), rs.getString("pass"),rs.getInt("access_level"),rs.getInt("office_id"),rs.getBoolean("is_active"),rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(rs,ptmt,con);
        }
        return user;
    }
    @Override
    public boolean register(String id, String pw, String name) {
        String sql = "INSERT INTO users VALUES (null,?,?,'card', ?, 1, null,'123car3333',1,default)";
        try (Connection con = DBUtil.getConnect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, pw);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    
}
