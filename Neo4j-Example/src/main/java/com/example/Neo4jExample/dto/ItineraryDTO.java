package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.ItineraryNode;
import lombok.Data;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class ItineraryDTO {
    private Long id;
    private Collection<CityDTO> cities;
    private String createdBy;
    private Collection<PoiDTO> points;
    private Integer timeToVisit;
    private Collection<CategoryDTO> categories;
    private String geoJson;

    public ItineraryDTO(ItineraryNode itineraryNode) {
        this.id = itineraryNode.getId();
        this.categories = itineraryNode.getCategories().stream().map(CategoryDTO::new).toList();
        this.createdBy = itineraryNode.getCreatedBy();
        this.points = itineraryNode.getPoints().stream().map(PoiDTO::new).toList();
        this.timeToVisit = itineraryNode.getTimeToVisit();
        this.geoJson = itineraryNode.getGeoJson();
        this.cities = itineraryNode.getCities().stream().map(CityDTO::new).toList();
    }
}
