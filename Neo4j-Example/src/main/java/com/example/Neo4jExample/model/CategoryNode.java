package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@NoArgsConstructor
@Data
@Node
public class CategoryNode {

    @Id
    private String name;

    public CategoryNode(String name) {
        this();
        this.name = name;
    }

}