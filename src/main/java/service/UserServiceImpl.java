package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import dto.MemberDTO;

public class UserServiceImpl implements UserService{
    private UserDAO dao;
    @Override
    public MemberDTO login(String id, String pw) {
        dao = new UserDAOImpl();
        MemberDTO user = dao.login(id,pw);
        return user;
    }
    
    @Override
    public boolean register(String id, String pw, String name) {
    	dao = new UserDAOImpl();
    	boolean register = dao.register(id, pw, name);
        return register;
    }
}
