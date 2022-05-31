package com.example.Neo4jExample.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Objects;

@RelationshipProperties
@Data
@NoArgsConstructor
public class CatHasTag{
    @RelationshipId
    private Long id;
    @TargetNode
    private Tag tag;
    private String value;

    public CatHasTag(Tag tag) {
        this.tag = tag;
    }
}
