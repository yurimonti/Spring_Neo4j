package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a request of creation or modification of a POI
 */
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
    private Boolean accepted;
    private TimeSlot timeSlot;
    private Integer timeToVisit;
    private Address address;
    private Double ticketPrice;
    private URL link;
    private String username;

    private PointOfInterestNode pointOfInterestNode;
    @Relationship(type = "POI_HAS_TYPE")
    private Collection<PoiType> types;
    @Relationship(type = "POI_HAS_CONTACT")
    private Contact contact;
    @Relationship(type = "TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private Collection<PoiTagRel> tagValues;

    /*public PoiRequestNode(String name, String description, CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types){
        this.name = name;
        this.description = description;
        this.city = city;
        this.coordinate = coordinate;
        this.address = address;
        this.types = types;
        this.accepted = null;
    }*/

    /*public PoiRequestNode(String name, String description,CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types, Collection<PoiTagRel> tagValues){
        this(name,description,city,coordinate,address,types);
        this.tagValues = tagValues;
    }*/

    /*public PoiRequestNode(String name, String description,CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types, Collection<PoiTagRel> tagValues,TimeSlot timeSlot,
                          Integer timeToVisit, Double ticketPrice,String username,Contact contact) {
        this(name,description,city,coordinate,address,types,tagValues);
        this.timeSlot = timeSlot;
        this.timeToVisit = timeToVisit;
        this.ticketPrice = ticketPrice;
        this.username = username;
        this.contact = contact;
    }*/

    public PoiRequestNode(String username, String name, String description, CityNode city, Coordinate coord, Address address, Integer timeToVisit, Double ticketPrice){
        this.username = username;
        this.name = name;
        this.description = description;
        this.city = city;
        this.coordinate = coord;
        this.address = address;
        this.timeToVisit = timeToVisit;
        this.ticketPrice = ticketPrice;
        this.accepted = null;
        this.timeSlot = null;
        this.types = new ArrayList<>();
        this.tagValues = new ArrayList<>();
        this.pointOfInterestNode = null;
    }



}
