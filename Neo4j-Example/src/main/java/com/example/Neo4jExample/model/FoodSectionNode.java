package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
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
        this();
        this.name = name;
        this.dishes = new ArrayList<>();
    }

    public FoodSectionNode(String name,DishNode ...dishes) {
        this(name);
        this.dishes.addAll(Arrays.asList(dishes));
    }
}