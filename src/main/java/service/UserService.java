package service;

import dto.MemberDTO;

public interface UserService {
    MemberDTO login(String id, String pw);
    
    boolean register(String id, String pw, String name);
}
