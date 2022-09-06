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
     * Get a user by its username
     * @param username username of the user
     * @return the user
     */
    public UserNode getUserByUsername(String username) {
        return this.userNodeRepository.findByUsername(username);
    }

    /**
     * Find if a user has a specific role
     * @param username the username of the user
     * @param roleName the role to search
     * @return true if the user has the role, false otherwise
     */
    public boolean userHasRole(String username,String roleName) {
        return this.userNodeRepository.findByUsername(username).getRoles().contains(this.getRoleFromName(roleName));
    }

    /**
     * Get a role given its name
     * @param name name of the role
     * @return the role
     */
    private UserRole getRoleFromName(String name) {
        return this.userRoleRepository.findByName(name);
    }

    /**
     * Get an ente given its username
     * @param username username of the ente
     * @return the ente
     */
    public Ente getEnteFromUser(String username){
        return this.enteRepository.findAll().stream().filter(ente -> ente.getUser()
                        .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }

    /**
     * Get a classic user given its username
     * @param username username of the classic user
     * @return the classic user
     */
    public ClassicUserNode getClassicUserFromUser(String username){
        log.info("getting username for an ClassicUserNode {}", username);
        return this.classicUserRepository.findAll().stream().filter(classicUserNode -> classicUserNode.getUser()
                .equals(this.getUserByUsername(username))).findFirst().orElseThrow();
    }


    /**
     * Add an itinerary to the list of itineraries created by a classic user
     * @param user the classic user
     * @param toAdd the itinerary to add
     */
    public void addItineraryToUser(ClassicUserNode user, ItineraryNode toAdd) {
        user.getItineraries().add(toAdd);
        this.classicUserRepository.save(user);
    }
}