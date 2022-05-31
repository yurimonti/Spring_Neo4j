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
public class MenuNode {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "HAS_FOODSECTION",direction = Relationship.Direction.OUTGOING)
    private Collection<FoodSectionNode> foodSections;

    public MenuNode() {
        this.foodSections = new ArrayList<>();
    }
}