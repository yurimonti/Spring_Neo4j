package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Collection;

@Node
@Data
@NoArgsConstructor
public class ClassicUserNode {
    @Id
    @GeneratedValue
    private Long id;
    private UserNode user;
    private Collection<PoiRequestNode> poiRequests;
    private Collection<ItineraryNode> itineraries;
    private Collection<ItineraryRequestNode> itineraryRequests;

    public ClassicUserNode(UserNode user) {
        this.user = user;
        this.poiRequests = new ArrayList<>();
        this.itineraries = new ArrayList<>();
        this.itineraryRequests = new ArrayList<>();
    }
}
