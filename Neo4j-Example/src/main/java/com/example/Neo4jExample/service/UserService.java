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

    public UserNode getUserByUsername(String username) {
        return this.userNodeRepository.findByUsername(username);
    }

    public Ente getEnteFromUser(String username){
        return enteRepository.findAll().stream().filter(ente -> ente.getUser()
                        .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }
}