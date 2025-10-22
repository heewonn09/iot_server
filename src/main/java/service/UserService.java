package service;

import dto.MemberDTO;

public interface UserService {
    int login(String id, String pw);
}
