package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.PointOfInterestNode;
import lombok.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class PoiDTO {
    private Long id;
    private String name;
    private String description;
    private CoordinateDTO coordinate;
    private TimeSlotDTO hours;
    private Double timeToVisit;
    private AddressDTO address;
    private Double ticketPrice;
    private Collection<String> contributors;
    private URL link;
    private Collection<PoiTypeDTO> types;
    private ContactDTO contact;
    private Collection<PoiTagRelDTO> tagValues;

    public PoiDTO(PointOfInterestNode poi){
        this.id = poi.getId();
        this.name = poi.getName();
        this.description = poi.getDescription();
        this.types = new ArrayList<>();
        this.types.addAll(poi.getTypes().stream().map(PoiTypeDTO::new).toList());
        this.tagValues = poi.getTagValues().stream().map(PoiTagRelDTO::new).toList();
        this.coordinate = new CoordinateDTO(poi.getCoordinate());
        this.hours = new TimeSlotDTO(poi.getHours());
        this.ticketPrice = poi.getTicketPrice();
        this.contact = new ContactDTO(poi.getContact());
        this.timeToVisit = poi.getTimeToVisit();
        this.address = new AddressDTO(poi.getAddress());
        this.contributors = poi.getContributors();
        this.link = poi.getLink();
    }

}
