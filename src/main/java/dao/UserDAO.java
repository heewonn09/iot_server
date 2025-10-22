package dao;

import DBUtil.DBUtil;
import dto.MemberDTO;

public interface UserDAO {
    public MemberDTO login(String id, String pw);
}
