package com.example.Neo4jExample.model2;

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
@NoArgsConstructor
@Node
public class PointOfInterestNode {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private Coordinate coordinate;
    private TimeSlot timeSlot;
    private Integer timeToVisit;//tempo medio durata di visita
    private Address address;

    private Boolean needTicket;
    private URL link;//url sito

    @Relationship(type = "POI_HAS_TYPE")
    private Collection<PoiType> types;
    @Relationship(type = "POI_HAS_CONTACT")
    private Contact contact;

    @Relationship(type = "TAG_VALUE",direction = Relationship.Direction.OUTGOING)
    private Collection<PoiTagRel> tagValues;


    public PointOfInterestNode(String name, String description, Coordinate coordinate) {
        this();
        this.name = name;
        this.description = description;
        this.coordinate= coordinate;
        this.types = new ArrayList<>();
    }

    public PointOfInterestNode(String name, String description, Coordinate coordinate,Contact contact,TimeSlot timeSlot,
                               Integer timeToVisit,Address address,URL link) {
        this(name,description,coordinate);
        this.contact= contact;
        this.timeSlot= timeSlot;
        this.timeToVisit = timeToVisit;
        this.address=address;
        this.link = link;
    }

}