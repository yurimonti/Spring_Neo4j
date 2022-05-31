package com.example.Neo4jExample.model2;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;
@Data @NoArgsConstructor @Node
public class CityNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "CONTAINED",direction = Relationship.Direction.INCOMING)
    private Collection<PointOfInterestNode> pointOfInterests;
    @Relationship(type = "CITY_HAS_COORDS",direction = Relationship.Direction.OUTGOING)
    private Coordinate coordinate;

    public CityNode(String name,Coordinate coordinate) {
        this();
        this.name = name;
        this.coordinate = coordinate;
        this.pointOfInterests = new ArrayList<>();
    }
}
