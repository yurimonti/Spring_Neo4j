package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@NoArgsConstructor
public class StartToTag {
    @RelationshipId
    private Long id;
    @TargetNode
    private Tag tag;

    private String stringValue;
    private Boolean boolValue;

    public StartToTag(Tag tag) {
        this.tag = tag;
    }
}
