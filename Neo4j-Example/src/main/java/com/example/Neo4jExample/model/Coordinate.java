package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@NoArgsConstructor
@Data
@Node
public class Coordinate {

    @Id
    @GeneratedValue
    private Long id;

    private double longitudine;
    private double latitudine;

    public Coordinate(double longitudine, double latitudine){
        this.longitudine = longitudine;
        this.latitudine = latitudine;
    }
}
