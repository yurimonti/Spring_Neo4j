package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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
