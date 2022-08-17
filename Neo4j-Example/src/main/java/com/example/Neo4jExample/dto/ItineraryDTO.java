package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.ItineraryNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Data
public class ItineraryDTO {
    private Long id;
    private String name;
    private String description;
    private Collection<CityDTO> cities;
    private String createdBy;
    private Collection<ItineraryRelPoiDTO> points;
    private Double timeToVisit;
    private Collection<CategoryDTO> categories;
    private Collection<String> geoJsonList;

    private Boolean isDefault;

    public ItineraryDTO() {
        this.id = 0L;
        this.name = "";
        this.description = "";
        this.categories = new ArrayList<>();
        this.timeToVisit = 0.0;
        this.points = new ArrayList<>();
        this.createdBy = "";
        this.cities = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
    }

    public ItineraryDTO(ItineraryNode itineraryNode) {
        this.id = itineraryNode.getId();
        this.name = itineraryNode.getName();
        this.description = itineraryNode.getDescription();
        this.categories = itineraryNode.getCategories().stream().map(CategoryDTO::new).toList();
        this.createdBy = itineraryNode.getCreatedBy();
        this.points = itineraryNode.getPoints().stream().map(ItineraryRelPoiDTO::new).sorted(Comparator.comparingInt(ItineraryRelPoiDTO::getIndex)).collect(Collectors.toList());
        this.timeToVisit = itineraryNode.getTimeToVisit();
        this.geoJsonList = itineraryNode.getGeoJsonList();
        this.cities = itineraryNode.getCities().stream().map(CityDTO::new).toList();
        this.isDefault = itineraryNode.getIsDefault();
    }
}
