package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Represents a tag
 */
@Data
@NoArgsConstructor
@Node
public class TagNode {
    @Id
    private String name;
    private Boolean isBooleanType;

    public TagNode(String name, Boolean isBooleanType) {
        this.name = name;
        this.isBooleanType = isBooleanType;
    }
}
