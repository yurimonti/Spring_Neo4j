package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.model.UserNode;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.UserNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserNodeRepository userNodeRepository;
    private final EnteRepository enteRepository;

    /**
     * get user by username
     * @param username of User
     * @return User by his username
     */
    public UserNode getUserByUsername(String username) {
        return this.userNodeRepository.findByUsername(username);
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
}