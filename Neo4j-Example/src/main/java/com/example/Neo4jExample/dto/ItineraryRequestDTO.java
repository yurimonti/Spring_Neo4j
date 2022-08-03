package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.ItineraryRequestNode;
import com.example.Neo4jExample.model.PointOfInterestNode;
import lombok.Data;

import java.util.Collection;
import java.util.Objects;

@Data
public class ItineraryRequestDTO {
    private Long id;
    private Collection<PoiDTO> points;
    private Collection<CityDTO> cities;
    private String createdBy;
    private StatusEnum status;
    private Integer timeToVisit;
    private String geojson;

    public ItineraryRequestDTO(ItineraryRequestNode itineraryRequestNode){
        if (Objects.isNull(itineraryRequestNode.getAccepted())) this.status = StatusEnum.PENDING;
        else if (itineraryRequestNode.getAccepted()) this.status = StatusEnum.ACCEPTED;
        else this.status = StatusEnum.REJECTED;
        this.id = itineraryRequestNode.getId();
        this.points = itineraryRequestNode.getPoints().stream().map(PoiDTO::new).toList();
        this.cities = itineraryRequestNode.getCities().stream().map(CityDTO::new).toList();
        this.createdBy = itineraryRequestNode.getCreatedBy();
        this.timeToVisit = itineraryRequestNode.getTimeToVisit();
        this.geojson = itineraryRequestNode.getGeojson();
    }
}
