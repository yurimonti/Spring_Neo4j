package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
@Data
@NoArgsConstructor
public class PoiRequestDTO {
    private Long id;
    private String name;
    private String description;
    private CityDTO city;
    private Coordinate coordinate;
    private TimeSlot timeSlot;
    private Integer timeToVisit;
    private Address address;
    private Boolean needTicket;
    private URL link;
    private String username;
    // pois id
    private Long poiId;
    private Collection<PoiTypeDTO> types;
    private Contact contact;
    private Collection<PoiTagRelDTO> tagValues;

    private void fillTagsValues(PoiRequestNode poiRequestNode){
        this.tagValues.addAll(poiRequestNode.getTagValues().stream().map(PoiTagRelDTO::new).toList());
    }
    public PoiRequestDTO(PoiRequestNode poiRequestNode){
        this.id = poiRequestNode.getId();
        this.name = poiRequestNode.getName();
        this.description = poiRequestNode.getDescription();
        this.types = new ArrayList<>();
        this.types.addAll(poiRequestNode.getTypes().stream().map(PoiTypeDTO::new).toList());
        this.tagValues = new ArrayList<>();
        this.city = new CityDTO(poiRequestNode.getCity());
        fillTagsValues(poiRequestNode);
    }
}
