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
@RequiredArgsConstructor
@Data
@Node
public class Ente {
    @Id
    @GeneratedValue(GeneratedValue.UUIDGenerator.class)
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String username;
    @Relationship(type = "MANAGES_THIS",direction = Relationship.Direction.OUTGOING)
    private CityNode city;

}
