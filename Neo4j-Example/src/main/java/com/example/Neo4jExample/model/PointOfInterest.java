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

    private Double lat;

    private Double lon;

    private Object orari; //TODO

    private int durata; //TODO

    //private Map<String,String> inputText;
    private List<String> inputTextNames;
    private List<String> inputTextValues;

    //private Map<String,Boolean> inputSelect;
    private List<String> inputSelectNames;
    private List<Boolean> inputSelectValues;


    @Relationship(type = "POI_HAS_SOME")
    private Collection<Category> categories;

    public PointOfInterest(String name, String description, Double lat, Double lon) {
        this();
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.categories = new ArrayList<>();
        this.inputTextNames = new ArrayList<>();
        this.inputTextValues = new ArrayList<>();
        this.inputSelectNames = new ArrayList<>();
        this.inputSelectValues = new ArrayList<>();
        //this.inputSelect = new HashMap<>();
        //this.inputText = new HashMap<>();

    }

}
