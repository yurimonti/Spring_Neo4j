package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents an extra series of information for food related activities linked to a specific POI
 */
@Node
@Data
@NoArgsConstructor
public class RestaurantPoi {

    @Id @GeneratedValue
    private Long id;
    private Commerciante owner;
    private PointOfInterestNode poi;
    @Relationship(type = "HAS_MENU",direction = Relationship.Direction.OUTGOING)
    private MenuNode menu;

    public RestaurantPoi(Commerciante owner){
        this();
        this.owner = owner;
        this.menu = new MenuNode();
    }

    public RestaurantPoi(Commerciante owner,PointOfInterestNode poi){
        this();
        this.owner = owner;
        this.poi = poi;
        this.menu = new MenuNode();
    }
}
