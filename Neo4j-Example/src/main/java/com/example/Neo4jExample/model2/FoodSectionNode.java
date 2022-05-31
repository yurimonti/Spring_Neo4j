package com.example.Neo4jExample.model2;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Node
@NoArgsConstructor
public class FoodSectionNode {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Relationship(type = "HAS_DISHE",direction = Relationship.Direction.OUTGOING)
    private Collection<DishNode> dishes;

    public FoodSectionNode(String name) {
        this.name = name;
        this.dishes = new ArrayList<>();
    }
}