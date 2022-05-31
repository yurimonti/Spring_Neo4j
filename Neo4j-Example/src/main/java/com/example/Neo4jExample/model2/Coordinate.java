package com.example.Neo4jExample.model2;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data @NoArgsConstructor @Node
public class Coordinate {
    @Id @GeneratedValue
    private Long id;
    private Double lat;
    private Double lon;
    private Double alt;

    public Coordinate(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Coordinate(Double lat, Double lon,Double alt){
        this(lat,lon);
        this.alt=alt;
    }
}
