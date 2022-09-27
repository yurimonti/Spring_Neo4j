package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents an itinerary request
 */
@Node
@Data
public class ItineraryRequestNode {
    @Id @GeneratedValue
    private Long id;
    private String name;

    private String description;
    private Collection<ItineraryRelPoi> points;
    private Collection<CityNode> cities;
    private Boolean accepted;
    private Collection<String> consensus;
    private String createdBy;
    private Double timeToVisit;
    private Collection<String> geoJsonList;

    public ItineraryRequestNode(){
        this.cities = new ArrayList<>();
        this.points = new ArrayList<>();
        this.consensus = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
        this.accepted = null;
    }

    public ItineraryRequestNode(String name, String description, String createdBy) {
        this();
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    public ItineraryRequestNode(String name, String description, Collection<ItineraryRelPoi> points, Collection<String> geoJsonList, String createdBy, CityNode ...cities){
        this();
        this.name = name;
        this.description = description;
        this.points = points;
        this.geoJsonList = geoJsonList;
        this.createdBy = createdBy;
        this.cities = Arrays.stream(cities).toList();
    }

}
