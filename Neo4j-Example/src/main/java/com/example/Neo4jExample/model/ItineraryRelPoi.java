package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * Represents a relationship between an itinerary and
 * a point of interest contained in it, saving a value to know the position of it
 * considering the others
 */
@RelationshipProperties
@Data
@NoArgsConstructor
public class ItineraryRelPoi {
    @RelationshipId
    private Long id;
    @TargetNode
    private PointOfInterestNode poi;
    private Integer index;

    public ItineraryRelPoi(PointOfInterestNode poi, Integer index) {
        this.poi = poi;
        this.index = index;
    }
}
