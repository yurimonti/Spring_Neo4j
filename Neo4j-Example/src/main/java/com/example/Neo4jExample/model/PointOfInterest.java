package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Collection;

@Data
@NoArgsConstructor
@Node
public class PointOfInterest {
    @Id @GeneratedValue
    private Long id;

    private String name;

    /*private City city;*/

    private String description;

    private Long lat;

    private Long lon;
    @Relationship(type = "POI_HAS_SOME")
    private Collection<Category> categories;

    public PointOfInterest(String name, String description, /*City city,*/ Long lat, Long lon) {
        this();
        this.name = name;
        this.description = description;
        /*this.city = city;*/
        this.lat = lat;
        this.lon = lon;
    }
}
