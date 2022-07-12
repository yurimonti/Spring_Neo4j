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
public class PointOfInterestNode {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Coordinate coordinate;
    private TimeSlot hours;
    private Integer timeToVisit;//tempo medio durata di visita
    private Address address;
    private Double ticketPrice;
    private Collection<String> contributors;
    private URL link;//url sito

    @Relationship(type = "POI_HAS_TYPE")
    private Collection<PoiType> types;

    @Relationship(type = "POI_HAS_CONTACT")
    private Contact contact;

    @Relationship(type = "TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private Collection<PoiTagRel> tagValues;

    public PointOfInterestNode(){
        this.types = new ArrayList<>();
        this.tagValues = new ArrayList<>();
        this.contributors = new ArrayList<>();
    }

    public PointOfInterestNode(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public PointOfInterestNode(String name, String description, Coordinate coordinate, Address address) {
        this(name,description);
        this.coordinate = coordinate;
        this.address = address;
    }

    public PointOfInterestNode(String name, String description, Coordinate coordinate, Address address,
                               TimeSlot hours) {
        this(name,description,coordinate,address);
        this.hours = hours;
    }

    public PointOfInterestNode(String name, String description, Coordinate coordinate, Address address,
                               TimeSlot hours,Integer timeToVisit,Double ticketPrice,Contact contact) {
        this(name,description,coordinate,address,hours);
        this.timeToVisit = timeToVisit;
        this.ticketPrice = ticketPrice;
        this.contact = contact;
    }

    public PointOfInterestNode(String name, String description, Integer timeToVisit, URL link) {
        this(name,description);
        this.timeToVisit = timeToVisit;
        this.link = link;
    }

    public PointOfInterestNode(PoiRequestNode request){
        this();
        this.name = request.getName();
        this.description = request.getDescription();
        this.coordinate = request.getCoordinate();
        this.hours = request.getTimeSlot();
        this.timeToVisit = request.getTimeToVisit();
        this.address = request.getAddress();
        this.ticketPrice = request.getTicketPrice();
        this.contributors.add(request.getUsername());
        this.link = request.getLink();
        this.types = request.getTypes();
        this.contact = request.getContact();
        this.tagValues = request.getTagValues();
    }

}
