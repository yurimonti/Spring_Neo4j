package com.example.Neo4jExample.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
@Node
public class MacroCategory {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public MacroCategory(String name) {
        this();
        this.name = name;
    }

}
