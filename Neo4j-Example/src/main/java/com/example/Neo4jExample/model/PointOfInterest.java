package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.*;

@Data
@NoArgsConstructor
@Node
public class PointOfInterest {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Object orari; //TODO

    private int durata; //TODO
    
    private List<String> inputTextNames;
    private List<String> inputTextValues;

    private List<String> inputSelectNames;
    private List<Boolean> inputSelectValues;


    @Relationship(type = "POI_HAS_COORDINATES",direction = Relationship.Direction.OUTGOING)
    private Coordinate coordinate;

    @Relationship(type = "POI_HAS_SOME",direction = Relationship.Direction.OUTGOING)
    private Collection<Category> categories;

    public PointOfInterest(String name, String description) {
        this();
        this.name = name;

        this.description = description;
        this.coordinate = new Coordinate();
        this.categories = new ArrayList<>();
        this.inputTextNames = new ArrayList<>();
        this.inputTextValues = new ArrayList<>();
        this.inputSelectNames = new ArrayList<>();
        this.inputSelectValues = new ArrayList<>();

    }

}
