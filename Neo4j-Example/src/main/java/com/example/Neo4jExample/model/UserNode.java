package com.example.Neo4jExample.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Node
@Data
public class UserNode {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String username;
    private Collection<UserRole> roles;

    public UserNode() {
        this.roles = new ArrayList<>();
    }

    public UserNode(String name,String surname,String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserNode(String name,String surname,String email, String password, String username, UserRole ...roles) {
        this(name,surname,email, password, username);
        this.roles = Arrays.stream(roles).toList();
    }
}
