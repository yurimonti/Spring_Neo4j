package com.example.Neo4jExample.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Data
@Node
public class MenuNode {
    @Id @GeneratedValue
    private Long id;
    @Relationship(type = "HAS_FOODSECTION",direction = Relationship.Direction.OUTGOING)
    private Collection<FoodSectionNode> foodSections;

    private Integer priceValue;

    public MenuNode() {
        this.foodSections = new ArrayList<>();
    }
    public MenuNode(Integer priceValue,FoodSectionNode ...foodSections){
        this();
        this.priceValue = priceValue;
        this.foodSections.addAll(Arrays.asList(foodSections));
    }
}