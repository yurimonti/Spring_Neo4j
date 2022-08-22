package com.example.Neo4jExample.service.util;

import com.example.Neo4jExample.model.UserNode;
import com.example.Neo4jExample.model.UserRole;

import java.util.List;

public interface UserServiceInterface {
    UserNode saveUser(UserNode user);
    UserRole saveRole(UserRole userRole);
    void addRoleToUser(String username,String roleName);
    List<UserNode> getUsers();
}
