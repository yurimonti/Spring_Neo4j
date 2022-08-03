package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
@Node
@Data
@NoArgsConstructor
public class ItineraryRequestNode {
    @Id @GeneratedValue
    private Long id;
    private Collection<PointOfInterestNode> points;
    private Collection<CityNode> cities;
    private Boolean accepted;
    private Collection<String> consensus;
    private String createdBy;
    private Integer timeToVisit;
    private String geojson;

    public ItineraryRequestNode(Collection<PointOfInterestNode> points, String geoJson, String createdBy, CityNode ...cities){
        this.consensus = new ArrayList<>();
        this.points = points;
        this.geojson = geoJson;
        this.createdBy = createdBy;
        this.cities = Arrays.stream(cities).toList();
        this.accepted = null;
    }

}
