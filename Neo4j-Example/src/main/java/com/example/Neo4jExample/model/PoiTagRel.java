package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@NoArgsConstructor
public class PoiTagRel {
    @RelationshipId
    private Long id;

    @TargetNode
    private TagNode tag;

    private Boolean booleanValue;

    private String stringValue;

    public PoiTagRel(TagNode tag) {
        this.tag = tag;
    }
}
