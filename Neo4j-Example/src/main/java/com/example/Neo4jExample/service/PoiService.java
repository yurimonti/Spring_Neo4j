package com.example.Neo4jExample.service;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.util.MyGsonSerializer;
import com.example.Neo4jExample.service.util.MySerializer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoiService {
    private final PointOfIntRepository pointOfIntRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final UtilityService utilityService;

    private final CityRepository cityRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CoordinateRepository coordinateRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;

    public PointOfInterestNode findPoiById(Long id){
        return this.pointOfIntRepository.findById(id).orElse(null);
    }

    public void savePoiInACity(CityNode where,PointOfInterestNode toSave){
        where.getPointOfInterests().add(toSave);
        this.cityRepository.save(where);
    }

/*    private void clearTimeSlot(TimeSlot timeToClear) {
        timeToClear.setMonday(new ArrayList<>());
        timeToClear.setTuesday(new ArrayList<>());
        timeToClear.setWednesday(new ArrayList<>());
        timeToClear.setThursday(new ArrayList<>());
        timeToClear.setFriday(new ArrayList<>());
        timeToClear.setSaturday(new ArrayList<>());
        timeToClear.setSunday(new ArrayList<>());
        this.timeSlotRepository.save(timeToClear);
    }*/

    private TimeSlot copyTimeSlot(TimeSlot toCopy){
        TimeSlot timeSlot = new TimeSlot(toCopy.getMonday(),toCopy.getTuesday(),toCopy.getWednesday(),toCopy.getThursday(),
                toCopy.getFriday(),toCopy.getSaturday(),toCopy.getSunday());
        this.timeSlotRepository.save(timeSlot);
        return timeSlot;
    }

    private Collection<PoiTagRel> copyPoiTagRel(Collection<PoiTagRel> toCopy){
        Collection<PoiTagRel> results = new ArrayList<>();
        toCopy.forEach(value->{
            PoiTagRel poiTagRel = new PoiTagRel(value.getTag());
            if(value.getTag().getIsBooleanType())
                poiTagRel.setBooleanValue(value.getBooleanValue());
            else poiTagRel.setStringValue(value.getStringValue());
            results.add(poiTagRel);
        });
        return results;
    }

    private Address copyAddress(Address toCopy){
        Address result = new Address(toCopy.getStreet(), toCopy.getNumber());
        this.addressRepository.save(result);
        return result;
    }

    private Coordinate copyCoordinate(Coordinate toCopy){
        Coordinate result = new Coordinate(toCopy.getLat(), toCopy.getLon());
        this.coordinateRepository.save(result);
        return result;
    }

    private Contact copyContact(Contact toCopy){
        Contact result = new Contact(toCopy.getEmail(),toCopy.getCellNumber(),toCopy.getFax());
        this.contactRepository.save(result);
        return result;
    }

    /**
     * Create a Poi from a Poi Request
     * @param request from get paramaeters to create poi
     * @return created poi
     */
    public PointOfInterestNode createPoiFromRequest(PoiRequestNode request){
        PointOfInterestNode result = new PointOfInterestNode(request.getName(),request.getDescription(),
                this.copyCoordinate(request.getCoordinate()),this.copyTimeSlot(request.getHours()),
                request.getTimeToVisit(),this.copyAddress(request.getAddress()),request.getTicketPrice(),
                request.getLink(),request.getTypes(),this.copyContact(request.getContact()),
                this.copyPoiTagRel(request.getTagValues()));
        this.pointOfIntRepository.save(result);
        this.savePoiInACity(request.getCity(),result);
        return result;
    }

    /**
     * modifies Poi parameters from a request
     * @param request to get modifies
     */
    public void modifyPoiFromRequest(PoiRequestNode request){
        PointOfInterestNode result = request.getPointOfInterestNode();
        result.setHours(copyTimeSlot(request.getHours()));
        result.setCoordinate(copyCoordinate(request.getCoordinate()));
        result.setTicketPrice(result.getTicketPrice());
        result.setContact(copyContact(request.getContact()));
        result.setName(request.getName());
        result.setDescription(request.getDescription());
        result.setTimeToVisit(request.getTimeToVisit());
        result.setAddress(copyAddress(request.getAddress()));
        result.getContributors().add(request.getUsername());
        result.setLink(request.getLink());
        result.setTypes(request.getTypes());
        result.setTagValues(copyPoiTagRel(request.getTagValues()));
        this.pointOfIntRepository.save(result);
        this.poiRequestRepository.save(request);
    }

    /**
     * create a PointOfInterestNode from a body request
     *
     * @param bodyFrom body request
     * @return PointOfInterestNode just created
     */
    public PointOfInterestNode createPoiFromBody(Map<String, Object> bodyFrom) {
        PointOfInterestNode result = new PointOfInterestNode();
        String username = (String) bodyFrom.get("username");
        if (!Objects.isNull(username)) result.getContributors().add(username);
        String name = (String) bodyFrom.get("name");
        result.setName(name);
        String description = (String) bodyFrom.get("description");
        result.setDescription(description);
        Coordinate coordinate = this.utilityService.createCoordsFromString(
                (String) bodyFrom.get("lat"), (String) bodyFrom.get("lon"));
        result.setCoordinate(coordinate);
        String street = (String) bodyFrom.get("street");
        Integer number = Integer.parseInt((String) bodyFrom.get("number"));
        Address address = this.utilityService.createAddress(street, number);
        result.setAddress(address);
        Contact contact = new Contact((String) bodyFrom.get("email"), (String) bodyFrom.get("phone"),
                (String) bodyFrom.get("fax"));
        this.contactRepository.save(contact);
        result.setContact(contact);
        Integer timeToVisit = Integer.parseInt((String) bodyFrom.get("timeToVisit"));
        result.setTimeToVisit(timeToVisit);
        Double ticketPrice = Double.parseDouble((String) bodyFrom.get("price"));
        result.setTicketPrice(ticketPrice);
        TimeSlot timeSlot = this.utilityService.getTimeSlotFromBody(new TimeSlot(), bodyFrom);
        result.setHours(timeSlot);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        result.setTypes(poiTypes);
        result.setTagValues(this.utilityService.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags")));
        this.pointOfIntRepository.save(result);
        return result;
    }

    /**
     * modify a PointOfInterestNode with params contained in a body request
     *
     * @param poiToModify PointOfInterestNode to modify
     * @param bodyFrom    body request contained values to set
     */
    public void modifyPoiFromBody(PointOfInterestNode poiToModify, Map<String, Object> bodyFrom) {
        //FIXME: non salva i tagValues se modifico un poi
        String username = this.utilityService.getValueFromBody("username",bodyFrom);
        if(!Objects.isNull(username)) poiToModify.getContributors().add(username);
        String name = this.utilityService.getValueFromBody("name",bodyFrom);
        poiToModify.setName(name);
        String description = this.utilityService.getValueFromBody("description",bodyFrom);
        poiToModify.setDescription(description);
        Coordinate coordinate = new Coordinate(Double.parseDouble(this.utilityService.getValueFromBody("lat",bodyFrom)),
                Double.parseDouble(this.utilityService.getValueFromBody("lon",bodyFrom)));
        this.coordinateRepository.save(coordinate);
        poiToModify.setCoordinate(coordinate);
        Address address = new Address(this.utilityService.getValueFromBody("street",bodyFrom),
                Integer.parseInt(this.utilityService.getValueFromBody("number",bodyFrom)));
        this.addressRepository.save(address);
        poiToModify.setAddress(address);
        Contact contact = new Contact();
        contact.setEmail(this.utilityService.getValueFromBody("email",bodyFrom));
        contact.setCellNumber(this.utilityService.getValueFromBody("phone",bodyFrom));
        contact.setFax(this.utilityService.getValueFromBody("fax",bodyFrom));
        this.contactRepository.save(contact);
        poiToModify.setContact(contact);
        poiToModify.setTimeToVisit(Integer.parseInt(this.utilityService.getValueFromBody("timeToVisit",bodyFrom)));
        poiToModify.setTicketPrice(Double.parseDouble(this.utilityService.getValueFromBody("price",bodyFrom)));
        TimeSlot timeSlot = new TimeSlot();
        timeSlot = this.utilityService.getTimeSlotFromBody(timeSlot, bodyFrom);
        poiToModify.setHours(timeSlot);
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> this.poiTypeRepository.findById(a).isPresent())
                .map(a -> this.poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        poiToModify.setTypes(poiTypes);
        /*poiToModify.getTagValues().clear();
        poiToModify.getTagValues().addAll(createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags")));*/
        poiToModify.setTagValues(this.utilityService.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags")));
        this.pointOfIntRepository.save(poiToModify);
    }

        /*{
        CityNode city = null;
        PointOfInterestNode pointOfInterestNode;
        String poi = getValueFromBody("poi", bodyFrom);
        if (poi != null) {
            pointOfInterestNode = pointOfIntRepository.findById(Long.parseLong(poi))
                    .orElse(null);
            if (!Objects.isNull(pointOfInterestNode))
                city = this.getCityOfPoi(pointOfInterestNode);
            else {
                MySerializer<CityDTO> cityDTOMySerializer = new MyGsonSerializer<>(new Gson());
                CityDTO cityDto = cityDTOMySerializer.deserialize(
                        cityDTOMySerializer.serialize(bodyFrom.get("city")), CityDTO.class);
                city = cityRepository.findById(cityDto.getId()).orElse(null);
            }
        }
        if(Objects.isNull(city)) return null;
        String username = getValueFromBody("username", bodyFrom);
        String name = getValueFromBody("name", bodyFrom);
        String description = getValueFromBody("description", bodyFrom);
        Coordinate coordinate = provaService.createCoordsFromString(
                getValueFromBody("lat", bodyFrom), getValueFromBody("lon", bodyFrom));
        String street = getValueFromBody("street", bodyFrom);
        Integer number = Integer.parseInt(getValueFromBody("number", bodyFrom));
        Address address = provaService.createAddress(street, number);
        Contact contact = new Contact(getValueFromBody("email", bodyFrom), getValueFromBody("phone", bodyFrom),
                getValueFromBody("fax", bodyFrom));
        contactRepository.save(contact);
        Integer timeToVisit = Integer.parseInt(getValueFromBody("timeToVisit", bodyFrom));
        Double ticketPrice = Double.parseDouble(getValueFromBody("price", bodyFrom));
        TimeSlot timeSlot = this.getTimeSlotFromBody(new TimeSlot(), bodyFrom);
        *//*Collection<String> types = (Collection<String>) body.get("types");*//*
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        Collection<PoiTagRel> tagRels = this.createPoiTagRel((Collection<Map<String, Object>>) bodyFrom.get("tags"));
        PoiRequestNode result = new PoiRequestNode(name, description, city, coordinate, address, poiTypes, tagRels, timeSlot,
                timeToVisit, ticketPrice, username, contact);
        this.poiRequestRepository.save(result);
        return result;}*/


   /* public PointOfInterestNode createPoiFromRequest(PoiRequestNode requestFrom) {
        PointOfInterestNode result = new PointOfInterestNode();
        result.setName(requestFrom.getName());
        result.setDescription(requestFrom.getDescription());
        result.setTimeToVisit(requestFrom.getTimeToVisit());
        result.setTicketPrice(requestFrom.getTicketPrice());
        result.setLink(requestFrom.getLink());
        result.setTypes(requestFrom.getTypes());
        result.getContributors().add(requestFrom.getUsername());
        TimeSlot requestHours = requestFrom.getHours();
        TimeSlot timeSlot = new TimeSlot(requestHours.getMonday(), requestHours.getTuesday(),
                requestHours.getWednesday(), requestHours.getThursday(), requestHours.getFriday(),
                requestHours.getSaturday(), requestHours.getSunday());
        this.timeSlotRepository.save(timeSlot);
        result.setHours(timeSlot);
        Collection<PoiTagRel> tagRelsToAdd = requestFrom.getTagValues();
        tagRelsToAdd.forEach(value -> {
            PoiTagRel poiTagRel = new PoiTagRel(value.getTag());
            if (value.getTag().getIsBooleanType())
                poiTagRel.setBooleanValue(value.getBooleanValue());
            else poiTagRel.setStringValue(value.getStringValue());
            result.getTagValues().add(poiTagRel);
        });
        Coordinate coordinate = new Coordinate(requestFrom.getCoordinate().getLat(),
                requestFrom.getCoordinate().getLon());
        this.coordinateRepository.save(coordinate);
        result.setCoordinate(coordinate);
        Address address = new Address(requestFrom.getAddress().getStreet(),
                requestFrom.getAddress().getNumber());
        this.addressRepository.save(address);
        result.setAddress(address);
        Contact requestContact = requestFrom.getContact();
        Contact contactToAdd = new Contact(requestContact.getEmail(), requestContact.getCellNumber(),
                requestContact.getFax());
        this.contactRepository.save(contactToAdd);
        result.setContact(contactToAdd);
        this.pointOfIntRepository.save(result);
        return result;
    }*/
}
