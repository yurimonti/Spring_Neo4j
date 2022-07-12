package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Data
@NoArgsConstructor
public class RestaurantPoi {

    @Id @GeneratedValue
    private Long id;
    private Commerciante owner;
    @Relationship(type = "HAS_MENU",direction = Relationship.Direction.OUTGOING)
    private MenuNode menu;

    public RestaurantPoi(Commerciante owner){
        this();
        this.owner = owner;
        this.menu = new MenuNode();
    }
}
