package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.ClassicUserNode;
import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.model.ItineraryNode;
import com.example.Neo4jExample.model.UserNode;
import com.example.Neo4jExample.repository.ClassicUserRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.UserNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserNodeRepository userNodeRepository;
    private final EnteRepository enteRepository;

    private final ClassicUserRepository classicUserRepository;

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

    public ClassicUserNode getClassicUserFromUser(String username){
        return this.classicUserRepository.findAll().stream().filter(classicUserNode -> classicUserNode.getUser()
                .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }


    public void addItineraryToUser(ClassicUserNode user, ItineraryNode toAdd) {
        user.getItineraries().add(toAdd);
        this.classicUserRepository.save(user);
    }
}