package com.example.Neo4jExample.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data @AllArgsConstructor @Node
public class TagNode {
    @Id
    private String name;
    private Boolean isBooleanType;
}
