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

@Node
@Data
@NoArgsConstructor
public class ItineraryNode {
    @Id @GeneratedValue
    private Long id;
    @Relationship(type = "ITINERARY_OF_CITY",direction = Relationship.Direction.OUTGOING)
    private Collection<CityNode> cities;

    private String createdBy;
    @Relationship(type = "ITINERARY_HAS_POI",direction = Relationship.Direction.OUTGOING)
    private Collection<PointOfInterestNode> points;

    private Integer timeToVisit;
    @Relationship(type = "ITINERARY_HAS_CATEGORY",direction = Relationship.Direction.OUTGOING)
    private Collection<CategoryNode> categories;
    private String geoJson;


    private void setRealCategory(Collection<CategoryNode> categories,
                                 Collection<PointOfInterestNode> pointOfInterestNodes){
        Collection<CategoryNode> categoriesNodes = new ArrayList<>();
        Collection<PoiType> poiTypes = new ArrayList<>();
        pointOfInterestNodes.forEach(pointOfInterestNode -> poiTypes.addAll(pointOfInterestNode.getTypes()));
        Collection<PoiType> pois = poiTypes.stream().distinct().toList();
        pois.forEach(poiType -> categoriesNodes.addAll(poiType.getCategories()));
        categories.addAll(categoriesNodes.stream().distinct().toList());
    }

    public ItineraryNode(Collection<PointOfInterestNode> points, String geoJson,String createdBy,CityNode ...cities) {
        this();
        this.cities = Arrays.stream(cities).toList();
        this.points = points;
        this.categories = new ArrayList<>();
        setRealCategory(this.categories,points);
        this.geoJson = geoJson;
        this.createdBy = createdBy;
    }
}
