package com.example.Neo4jExample.model2;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    private String name;
    private String surname;

    private String username;
    
    @Relationship(type = "MANAGES_THIS",direction = Relationship.Direction.OUTGOING)
    private CityNode city;

    public Ente( String name,  String surname, String username) {
        this.name = name;
        this.surname = surname;
        this.username = username;
    }
}
