package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents an itinerary
 */
@Node
@Data
public class ItineraryNode {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;
    @Relationship(type = "ITINERARY_OF_CITY",direction = Relationship.Direction.OUTGOING)
    private Collection<CityNode> cities;

    private String createdBy;
    @Relationship(type = "ITINERARY_HAS_POI",direction = Relationship.Direction.OUTGOING)
    private Collection<ItineraryRelPoi> points;

    private Double timeToVisit;
    @Relationship(type = "ITINERARY_HAS_CATEGORY",direction = Relationship.Direction.OUTGOING)
    private Collection<CategoryNode> categories;
    //private String geoJson;
    private Collection<String> geoJsonList;
    private Boolean isDefault;


    private void setRealCategory(Collection<CategoryNode> categories,
                                 Collection<PointOfInterestNode> pointOfInterestNodes){
        Collection<CategoryNode> categoriesNodes = new ArrayList<>();
        Collection<PoiType> poiTypes = new ArrayList<>();
        pointOfInterestNodes.forEach(pointOfInterestNode -> poiTypes.addAll(pointOfInterestNode.getTypes()));
        Collection<PoiType> pois = poiTypes.stream().distinct().toList();
        pois.forEach(poiType -> categoriesNodes.addAll(poiType.getCategories()));
        categories.addAll(categoriesNodes.stream().distinct().toList());
    }

    public ItineraryNode(){
        this.categories = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
        this.points = new ArrayList<>();
        this.cities = new ArrayList<>();
    }

    public ItineraryNode(String name, String description, String createdBy, Boolean isDefault) {
        this();
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.isDefault = isDefault;
    }

    public ItineraryNode(String name, String description, Collection<ItineraryRelPoi> points, Collection<String> geoJsonList,
                         String createdBy, Boolean isDefault, CityNode ...cities) {
        this();
        this.name = name;
        this.description = description;
        this.cities = Arrays.stream(cities).toList();
        //this.points = points;
        this.points = points;
        setRealCategory(this.categories,points.stream().map(ItineraryRelPoi::getPoi).toList());
        this.isDefault = isDefault;
        //this.geoJson = geoJson;
        this.geoJsonList = geoJsonList;
        this.createdBy = createdBy;
    }
}
