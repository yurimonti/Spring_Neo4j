package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a city
 */
@Data @NoArgsConstructor @Node
public class CityNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "CITY_HAS_POI",direction = Relationship.Direction.OUTGOING)
    private Collection<PointOfInterestNode> pointOfInterests;

    @Relationship(type = "CITY_HAS_COORDS",direction = Relationship.Direction.OUTGOING)
    private Coordinate coordinate;

    public CityNode(String name) {
        this();
        this.name = name;
        this.pointOfInterests = new ArrayList<>();
    }
}
