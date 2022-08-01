package com.example.Neo4jExample.service;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoiRequestService {

    private final UtilityService utilityService;
    private final PoiRequestRepository poiRequestRepository;
    private final ContactRepository contactRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CityRepository cityRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final MySerializer<CityDTO> cityDTOMySerializer;


    private PoiRequestNode getBasicRequestFromBody(Map<String, Object> bodyFrom) {
        PoiRequestNode poiRequestNode = new PoiRequestNode();
        String username = this.utilityService.getValueFromBody("username", bodyFrom);
        String name = this.utilityService.getValueFromBody("name", bodyFrom);
        String description = this.utilityService.getValueFromBody("description", bodyFrom);
        Coordinate coordinate = this.utilityService.createCoordsFromString(
                this.utilityService.getValueFromBody("lat", bodyFrom), this.utilityService.getValueFromBody("lon", bodyFrom));
        String street = this.utilityService.getValueFromBody("street", bodyFrom);
        Integer number = Integer.parseInt(this.utilityService.getValueFromBody("number", bodyFrom));
        Address address = this.utilityService.createAddress(street, number);
        Contact contact = new Contact(this.utilityService.getValueFromBody("email", bodyFrom),
                this.utilityService.getValueFromBody("phone", bodyFrom),
                this.utilityService.getValueFromBody("fax", bodyFrom));
        this.contactRepository.save(contact);
        Integer timeToVisit = Integer.parseInt(this.utilityService.getValueFromBody("timeToVisit", bodyFrom));
        Double ticketPrice = Double.parseDouble(this.utilityService.getValueFromBody("price", bodyFrom));
        TimeSlot timeSlot = this.utilityService.getTimeSlotFromBody(new TimeSlot(), bodyFrom);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> this.poiTypeRepository.findById(a).isPresent())
                .map(a -> this.poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        Collection<PoiTagRel> tagRels = this.utilityService.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags"));
        poiRequestNode.setUsername(username);
        poiRequestNode.setName(name);
        poiRequestNode.setDescription(description);
        poiRequestNode.setCoordinate(coordinate);
        poiRequestNode.setAddress(address);
        poiRequestNode.setContact(contact);
        poiRequestNode.setTimeToVisit(timeToVisit);
        poiRequestNode.setTicketPrice(ticketPrice);
        poiRequestNode.setHours(timeSlot);
        poiRequestNode.setTypes(poiTypes);
        poiRequestNode.setTagValues(tagRels);
        return poiRequestNode;
    }

    public void setPoiToRequest(PoiRequestNode where, PointOfInterestNode toSet){
        where.setPointOfInterestNode(toSet);
        this.poiRequestRepository.save(where);
    }

    public void changeStatusToRequest(PoiRequestNode where,boolean toSet){
        where.setAccepted(toSet);
        this.poiRequestRepository.save(where);
    }

    public PoiRequestNode createAddRequestFromBody(Map<String, Object> bodyFrom) {
        CityDTO cityDto = this.cityDTOMySerializer.deserialize(
                this.cityDTOMySerializer.serialize(bodyFrom.get("city")), CityDTO.class);
        CityNode city = this.cityRepository.findById(cityDto.getId()).orElse(null);
        PoiRequestNode result = this.getBasicRequestFromBody(bodyFrom);
        result.setCity(city);
        this.poiRequestRepository.save(result);
        return result;
    }

    public PoiRequestNode createModifyRequestFromBody(Map<String, Object> bodyFrom){
        String poi = this.utilityService.getValueFromBody("poi",bodyFrom);
        if(poi == null) return null;
        PointOfInterestNode pointOfInterestNode = this.pointOfIntRepository.findById(Long.parseLong(poi))
                .orElse(null);
        if(Objects.isNull(pointOfInterestNode)) return null;
        CityNode city = this.utilityService.getCityOfPoi(pointOfInterestNode);
        PoiRequestNode result = this.getBasicRequestFromBody(bodyFrom);
        result.setPointOfInterestNode(pointOfInterestNode);
        result.setCity(city);
        this.poiRequestRepository.save(result);
        return result;
    }

    public Collection<PoiRequestNode> getFilteredRequests(Predicate<PoiRequestNode> filter){
        return this.poiRequestRepository.findAll().stream().filter(filter).toList();
    }

    public PoiRequestNode findRequestById(Long id) {
        if (this.poiRequestRepository.findById(id).isPresent()) return this.poiRequestRepository.findById(id).get();
        else return null;
    }
}
