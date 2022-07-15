package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a data transfer object for the class PoiRequestNode
 */
@Data
@NoArgsConstructor
public class PoiRequestDTO {
    private Long id;
    private StatusEnum status;
    private String name;
    private String description;
    private CityDTO city;
    private CoordinateDTO coordinate;
    private TimeSlotDTO timeSlot;
    private Integer timeToVisit;
    private AddressDTO address;
    private Double ticketPrice;
    private URL link;
    private String username;
    // pois id
    private Long poiId;
    private Collection<PoiTypeDTO> types;
    private Contact contact;
    private Collection<PoiTagRelDTO> tagValues;

    /**
     * Adds tags values and map status from a PoiRequestNode to the same DTO Class
     * @param poiRequestNode poiRequest to map values into the same DTO Class
     */
    private void fillTagsValues(PoiRequestNode poiRequestNode){
        this.tagValues.addAll(poiRequestNode.getTagValues().stream().map(PoiTagRelDTO::new).toList());
        if(Objects.isNull(poiRequestNode.getAccepted())) this.status = StatusEnum.PENDING;
            else if(poiRequestNode.getAccepted()) this.status = StatusEnum.ACCEPTED;
                else this.status = StatusEnum.REJECTED;
    }

    //TODO:aggiungere su ogni Request gli attributi mancanti
    public PoiRequestDTO(PoiRequestNode poiRequestNode){
        this.id = poiRequestNode.getId();
        this.name = poiRequestNode.getName();
        this.description = poiRequestNode.getDescription();
        this.types = new ArrayList<>();
        this.types.addAll(poiRequestNode.getTypes().stream().map(PoiTypeDTO::new).toList());
        this.tagValues = new ArrayList<>();
        this.city = new CityDTO(poiRequestNode.getCity());
        this.coordinate = new CoordinateDTO(poiRequestNode.getCoordinate());
        this.timeSlot = new TimeSlotDTO(poiRequestNode.getTimeSlot());
        this.username = poiRequestNode.getUsername();
        this.address = new AddressDTO(poiRequestNode.getAddress());
        this.ticketPrice = poiRequestNode.getTicketPrice();
        this.contact = poiRequestNode.getContact();
        this.timeToVisit = poiRequestNode.getTimeToVisit();
        fillTagsValues(poiRequestNode);

    }
}
