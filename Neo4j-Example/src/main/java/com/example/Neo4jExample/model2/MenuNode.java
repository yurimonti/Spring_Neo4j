package com.example.Neo4jExample.model2;

import com.example.Neo4jExample.model.FoodSection;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Node
public class MenuNode {
    private Collection<FoodSectionNode> foodSections;

    public MenuNode() {
        this.foodSections = new ArrayList<>();
    }
}