package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.net.URL;
import java.util.Collection;

@Data
@NoArgsConstructor
@Node
public class PoiRequestNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private CityNode city;
    private Coordinate coordinate;
    private TimeSlot timeSlot;
    private Integer timeToVisit;
    private Address address;
    private Boolean needTicket;
    private URL link;
    private String username;

    private PointOfInterestNode pointOfInterestNode;
    @Relationship(type = "POI_HAS_TYPE")
    private Collection<PoiType> types;
    @Relationship(type = "POI_HAS_CONTACT")
    private Contact contact;
    @Relationship(type = "TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private Collection<PoiTagRel> tagValues;

    public PoiRequestNode(String name, String description, CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types){
        this.name = name;
        this.description = description;
        this.city = city;
        this.coordinate = coordinate;
        this.address = address;
        this.types = types;
    }

    public PoiRequestNode(String name, String description,CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types, Collection<PoiTagRel> tagValues) {
        this(name,description,city,coordinate,address,types);
        this.tagValues = tagValues;
    }
}