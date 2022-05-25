package com.example.Neo4jExample.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@NoArgsConstructor
@Node
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private boolean isBooleanType;

    public Tag(String name, boolean bool) {
        this();
        this.name = name;
        this.isBooleanType = bool;
    }
}
