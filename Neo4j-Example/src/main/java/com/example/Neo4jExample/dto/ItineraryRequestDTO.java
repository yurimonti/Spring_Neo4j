package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.ItineraryRequestNode;
import com.example.Neo4jExample.model.PointOfInterestNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ItineraryRequestDTO {
    private Long id;
    private String name;
    private String description;
    private Collection<ItineraryRelPoiDTO> points;
    private Collection<CityDTO> cities;
    private String createdBy;
    private StatusEnum status;

    private Collection<String> consensus;
    private Double timeToVisit;
    private Collection<String> geoJsonList;

    public ItineraryRequestDTO(){
        this.points = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.consensus = new ArrayList<>();
        this.geoJsonList = new ArrayList<>();
    }

    public ItineraryRequestDTO(ItineraryRequestNode itineraryRequestNode){
        if (Objects.isNull(itineraryRequestNode.getAccepted())) this.status = StatusEnum.PENDING;
        else if (itineraryRequestNode.getAccepted()) this.status = StatusEnum.ACCEPTED;
        else this.status = StatusEnum.REJECTED;
        this.id = itineraryRequestNode.getId();
        this.name = itineraryRequestNode.getName();
        this.description = itineraryRequestNode.getDescription();
        this.points = itineraryRequestNode.getPoints().stream().map(ItineraryRelPoiDTO::new).sorted(Comparator.comparingInt(ItineraryRelPoiDTO::getIndex)).collect(Collectors.toList());
        this.cities = itineraryRequestNode.getCities().stream().map(CityDTO::new).toList();
        this.createdBy = itineraryRequestNode.getCreatedBy();
        this.timeToVisit = itineraryRequestNode.getTimeToVisit();
        this.geoJsonList = itineraryRequestNode.getGeoJsonList();
        this.consensus = itineraryRequestNode.getConsensus();
    }
}
