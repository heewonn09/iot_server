package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.MemberDTO;

public class UserServiceImpl implements UserService{
    private UserDAO dao;
    @Override
    public int login(String id, String pw) {
        dao = new UserDAOImpl();
        MemberDTO user = dao.login(id,pw);
        return user.getAccess_level();
    }
}
