package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
@Node
public class City {
    @Id @GeneratedValue
    private Long id;
    @NonNull
    private String name;
    @Relationship(type = "CONTAINED",direction = Relationship.Direction.INCOMING)
    private Collection<PointOfInterest> pointOfInterests;

    public City(String name) {
        this();
        this.name = name;
        this.pointOfInterests= new ArrayList<>();
    }
}
