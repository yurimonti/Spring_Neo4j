package com.example.Neo4jExample.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Represents a role that a user can have
 */
@Node
@Data
public class UserRole {
    @Id @GeneratedValue
    private Long id;
    private String name;

    public UserRole(String name) {
        this.name = name;
    }
}
