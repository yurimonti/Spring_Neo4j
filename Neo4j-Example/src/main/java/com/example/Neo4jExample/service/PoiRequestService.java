package com.example.Neo4jExample.service;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PoiRequestService {

    private final UtilityService utilityService;
    private final PoiRequestRepository poiRequestRepository;
    private final ContactRepository contactRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CityRepository cityRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final MySerializer<CityDTO> cityDTOMySerializer;

    /**
     * Save a point of interest request
     * @param toSave the request to save
     */
    public void saveRequest(PoiRequestNode toSave){
        this.poiRequestRepository.save(toSave);
    }

    /**
     * Create a point of interest request from a body
     * @param bodyFrom body of the http request that contains values
     * @return the new point of interest request
     */
    private PoiRequestNode getBasicRequestFromBody(Map<String, Object> bodyFrom) {
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
        Double timeToVisit = Double.parseDouble(this.utilityService.getValueFromBody("timeToVisit", bodyFrom));
        Double ticketPrice = Double.parseDouble(this.utilityService.getValueFromBody("price", bodyFrom));
        TimeSlot timeSlot = this.utilityService.getTimeSlotFromBody(new TimeSlot(),bodyFrom);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> this.poiTypeRepository.findByName(a).isPresent())
                .map(a -> this.poiTypeRepository.findByName(a).get())
                .collect(Collectors.toList());
        Collection<PoiTagRel> tagRels = this.utilityService.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags"));
        PoiRequestNode poiRequestNode = new PoiRequestNode(name,description,coordinate,timeSlot,timeToVisit,address,
                ticketPrice,username,poiTypes,contact,tagRels);
        log.info("tag and Value of request {}: {}",
                poiRequestNode.getName(), tagRels.stream().map(PoiTagRel::getBooleanValue).toList());
        poiRequestNode.getTagValues().addAll(tagRels);
        log.info("Basic Request {}",poiRequestNode.getName());
        this.poiRequestRepository.save(poiRequestNode);
        return poiRequestNode;
    }

    /**
     * Set a point of interest to the target request
     * @param target point of interest request
     * @param toSet point of interest to set
     */
    public void setPoiToRequest(PoiRequestNode target, PointOfInterestNode toSet){
        target.setPointOfInterestNode(toSet);
        this.poiRequestRepository.save(target);
    }

    /**
     * Set request to accepted or rejected
     * @param target point of interest request
     * @param toSet value to set
     */
    public void changeStatusToRequest(PoiRequestNode target,boolean toSet){
        target.setAccepted(toSet);
        this.poiRequestRepository.save(target);
    }

    /**
     * Create an add point of interest request from body
     * @param bodyFrom body of the http request that contains values
     * @return point of interest request created
     */
    public PoiRequestNode createAddRequestFromBody(Map<String, Object> bodyFrom) {
        CityDTO cityDto = this.cityDTOMySerializer.deserialize(
                this.cityDTOMySerializer.serialize(bodyFrom.get("city")), CityDTO.class);
        CityNode city = this.cityRepository.findById(cityDto.getId()).orElse(null);
        PoiRequestNode result = this.getBasicRequestFromBody(bodyFrom);
        result.setCity(city);
        this.poiRequestRepository.save(result);
        return result;
    }

    /**
     * Create a modify point of interest request from body
     * @param bodyFrom body of the http request that contains values
     * @return the point of interest request created
     * @throws NullPointerException if the point of interest is not found in the body
     */
    public PoiRequestNode createModifyRequestFromBody(Map<String, Object> bodyFrom)throws NullPointerException{
        String poi = this.utilityService.getValueFromBody("poi",bodyFrom);
        log.info("poiId: {}",poi);
        if(Objects.isNull(poi)) throw new NullPointerException("null value");
        PointOfInterestNode pointOfInterestNode = this.pointOfIntRepository.findById(Long.parseLong(poi))
                .orElseThrow(()->new NullPointerException("poi with id " + Long.parseLong(poi) + " not found"));
        CityNode city = this.utilityService.getCityOfPoi(pointOfInterestNode.getId());
        log.info("city of node {} : {}",pointOfInterestNode.getName(),city.getName());
        PoiRequestNode result = this.getBasicRequestFromBody(bodyFrom);
        log.info("request with types : {}",result.getTypes());
        result.setPointOfInterestNode(pointOfInterestNode);
        log.info("{} setted with types : {}",result.getPointOfInterestNode().getName(),result.getPointOfInterestNode()
                .getTypes());
        result.setCity(city);
        log.info("{} setted",city.getName());
        this.poiRequestRepository.save(result);
        return result;
    }

    /**
     * Get a collection of point of interest requests with a certain filter
     * @param filter the filter to apply
     * @return a collection of requests filtered
     */
    public Collection<PoiRequestNode> getFilteredRequests(Predicate<PoiRequestNode> filter){
        return this.poiRequestRepository.findAll().stream().filter(filter).toList();
    }

    /**
     * Get a point of interest request by its id
     * @param id id of the request
     * @return the request if present, null otherwise
     */
    public PoiRequestNode findRequestById(Long id) {
        if (this.poiRequestRepository.findById(id).isPresent()) return this.poiRequestRepository.findById(id).get();
        else return null;
    }
}
