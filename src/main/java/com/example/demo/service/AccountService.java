package com.example.demo.service;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;

import java.util.List;

public interface AccountService {

    User addNewUser(User user);
    Role addNewRole(Role role);
    void addRoleToUser(String username,String RoelName);
    User loadUserByUsername(String username);
    List<User> listUser();
}
