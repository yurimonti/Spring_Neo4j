package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@Node
@Data
@NoArgsConstructor
public class StartRel {
   @Id @GeneratedValue
   private Long id;

   private String name;

   @Relationship(type = "hasTag",direction = Relationship.Direction.OUTGOING)
   private Collection<StartToTag> tags;

    public StartRel(String name) {
        this.name = name;
        this.tags= new ArrayList<>();
    }
}
