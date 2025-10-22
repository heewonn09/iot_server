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
                where user_id = ? and pass = ?
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
                user = new MemberDTO(rs.getInt("id"), rs.getString("user_id"),rs.getString("pass"), rs.getString("name"), rs.getString("card_id"),rs.getInt("access_level"),rs.getInt(""),rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(rs,ptmt,con);
        }
        return user;
    }
}
