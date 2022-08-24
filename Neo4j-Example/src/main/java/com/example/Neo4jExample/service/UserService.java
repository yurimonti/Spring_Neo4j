package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.ClassicUserRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.UserNodeRepository;
import com.example.Neo4jExample.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserNodeRepository userNodeRepository;
    private final EnteRepository enteRepository;
    private final UserRoleRepository userRoleRepository;
    private final ClassicUserRepository classicUserRepository;

    /**
     * get user by username
     * @param username of User
     * @return User by his username
     */
    public UserNode getUserByUsername(String username) {
        return this.userNodeRepository.findByUsername(username);
    }

    public boolean userHasRole(String username,String roleName) {
        return this.userNodeRepository.findByUsername(username).getRoles().contains(this.getRoleFromName(roleName));
    }

    private UserRole getRoleFromName(String name) {
        return this.userRoleRepository.findByName(name);
    }

    /**
     * get ente by username
     * @param username of Ente
     * @return Ente by his username
     */
    public Ente getEnteFromUser(String username){
        return enteRepository.findAll().stream().filter(ente -> ente.getUser()
                        .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }

    public ClassicUserNode getClassicUserFromUser(String username){
        log.info("getting username for an ClassicUserNode {}", username);
        return this.classicUserRepository.findAll().stream().filter(classicUserNode -> classicUserNode.getUser()
                .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }


    public void addItineraryToUser(ClassicUserNode user, ItineraryNode toAdd) {
        user.getItineraries().add(toAdd);
        this.classicUserRepository.save(user);
    }
}