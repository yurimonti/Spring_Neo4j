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

@Data
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
    private TimeSlot hours;
    private Double timeToVisit;
    private Address address;
    private Double ticketPrice;
    private URL link;
    private String username;

    private PointOfInterestNode pointOfInterestNode;
    @Relationship(type = "REQUEST_HAS_TYPE")
    private Collection<PoiType> types;
    @Relationship(type = "REQUEST_HAS_CONTACT")
    private Contact contact;
    @Relationship(type = "REQUEST_TAG_VALUE")
    private Collection<PoiTagRel> tagValues;

    public PoiRequestNode(){
        this.types = new ArrayList<>();
        this.tagValues = new ArrayList<>();
    }

    public PoiRequestNode(String name, String description, Coordinate coordinate, TimeSlot hours, Double timeToVisit,
                          Address address, Double ticketPrice,String username, Collection<PoiType> types, Contact contact,
                          Collection<PoiTagRel> tagValues) {
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.hours = hours;
        this.timeToVisit = timeToVisit;
        this.address = address;
        this.ticketPrice = ticketPrice;
        this.username = username;
        this.types = types;
        this.contact = contact;
        this.tagValues = tagValues;
    }

    public PoiRequestNode(String name, String description, CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types){
        this.name = name;
        this.description = description;
        this.city = city;
        this.coordinate = coordinate;
        this.address = address;
        this.types = types;
        this.accepted = null;
    }

    public PoiRequestNode(String name, String description,CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types, Collection<PoiTagRel> tagValues){
        this(name,description,city,coordinate,address,types);
        this.tagValues = tagValues;
    }

    public PoiRequestNode(String name, String description,CityNode city, Coordinate coordinate, Address address,
                          Collection<PoiType> types, Collection<PoiTagRel> tagValues,TimeSlot timeSlot,
                          Double timeToVisit, Double ticketPrice,String username,Contact contact) {
        this(name,description,city,coordinate,address,types,tagValues);
        this.hours = timeSlot;
        this.timeToVisit = timeToVisit;
        this.ticketPrice = ticketPrice;
        this.username = username;
        this.contact = contact;
    }

    public PoiRequestNode(String name, String description, Coordinate coordinate, TimeSlot timeSlot, Double timeToVisit,
                          Address address, Double ticketPrice, String username, Collection<PoiType> poiTypes,
                          Contact contact) {
        this();
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.hours = timeSlot;
        this.timeToVisit = timeToVisit;
        this.address = address;
        this.ticketPrice = ticketPrice;
        this.username = username;
        this.types = poiTypes;
        this.contact = contact;
    }
}
