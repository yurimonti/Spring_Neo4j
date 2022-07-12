package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;

@NoArgsConstructor
@Data
@Node
public class Ente {
    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private UUID id;

    private UserNode user;
    
    @Relationship(type = "MANAGES_THIS",direction = Relationship.Direction.OUTGOING)
    private CityNode city;

    public Ente( UserNode user) {
        this.user = user;
    }
}
