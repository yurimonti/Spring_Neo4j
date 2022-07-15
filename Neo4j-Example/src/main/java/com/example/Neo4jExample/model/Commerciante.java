package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Represents a type of user
 */
@Node
@Data
@NoArgsConstructor
public class Commerciante {
    @Id @GeneratedValue
    private Long id;
    private UserNode user;

    public Commerciante(UserNode user) {
        this.user = user;
    }
}
