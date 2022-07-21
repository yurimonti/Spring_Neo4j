package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoiService {
    private final PointOfIntRepository pointOfIntRepository;
    private final ProvaService provaService;
    private final TagRepository tagRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CoordinateRepository coordinateRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;

    private void clearTimeSlot(TimeSlot timeToClear){
        timeToClear.setMonday(new ArrayList<>());
        timeToClear.setTuesday(new ArrayList<>());
        timeToClear.setWednesday(new ArrayList<>());
        timeToClear.setThursday(new ArrayList<>());
        timeToClear.setFriday(new ArrayList<>());
        timeToClear.setSaturday(new ArrayList<>());
        timeToClear.setSunday(new ArrayList<>());
    }

    public PointOfInterestNode createPoiFromBody(Map<String,Object> bodyFrom){
        PointOfInterestNode result = new PointOfInterestNode();
        String username = (String)bodyFrom.get("username");
        result.getContributors().add(username);
        String name = (String) bodyFrom.get("name");
        result.setName(name);
        String description = (String) bodyFrom.get("description");
        result.setDescription(description);
        Coordinate coordinate = provaService.createCoordsFromString(
                (String) bodyFrom.get("lat"), (String) bodyFrom.get("lon"));

        result.setCoordinate(coordinate);
        String street = (String) bodyFrom.get("street");
        Integer number = Integer.parseInt((String) bodyFrom.get("number"));
        Address address = provaService.createAddress(street, number);
        result.setAddress(address);
        Contact contact = new Contact((String) bodyFrom.get("email"), (String) bodyFrom.get("phone"),
                (String) bodyFrom.get("fax"));
        contactRepository.save(contact);
        result.setContact(contact);
        Integer timeToVisit = Integer.parseInt((String) bodyFrom.get("timeToVisit"));
        result.setTimeToVisit(timeToVisit);
        Double ticketPrice = Double.parseDouble((String) bodyFrom.get("price"));
        result.setTicketPrice(ticketPrice);
        TimeSlot timeSlot = new TimeSlot();
        Collection<String> monday = (Collection<String>) bodyFrom.get("monday");
        Collection<String> tuesday = (Collection<String>) bodyFrom.get("tuesday");
        Collection<String> wednesday = (Collection<String>) bodyFrom.get("wednesday");
        Collection<String> thursday = (Collection<String>) bodyFrom.get("thursday");
        Collection<String> friday = (Collection<String>) bodyFrom.get("friday");
        Collection<String> saturday = (Collection<String>) bodyFrom.get("saturday");
        Collection<String> sunday = (Collection<String>) bodyFrom.get("sunday");
        if (!monday.isEmpty()) monday.forEach(s -> timeSlot.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> timeSlot.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> timeSlot.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> timeSlot.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> timeSlot.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> timeSlot.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> timeSlot.getSunday().add((LocalTime.parse(s))));
        timeSlotRepository.save(timeSlot);
        result.setHours(timeSlot);
        /*Collection<String> types = (Collection<String>) body.get("types");*/
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        result.setTypes(poiTypes);
        Collection<Map<String, Object>> poiTagRels = (Collection<Map<String, Object>>) bodyFrom.get("tags");
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String, Object> map : poiTagRels) {
            String tag = (String) map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if (!Objects.isNull(tagNode)) {
                if (tagNode.getIsBooleanType()) {
                    Boolean value = (Boolean) map.get("value");
                    poiTagRel.setBooleanValue(value);
                } else poiTagRel.setStringValue((String) map.get("value"));
            }
            values.add(poiTagRel);
        }
        result.setTagValues(values);
        this.pointOfIntRepository.save(result);
        return result;
    }

    public PointOfInterestNode modifyPoiFromBody(PointOfInterestNode poiToModify, Map<String,Object> bodyFrom){
        String username = (String)bodyFrom.get("username");
        poiToModify.getContributors().add(username);
        String name = (String) bodyFrom.get("name");
        poiToModify.setName(name);
        String description = (String) bodyFrom.get("description");
        poiToModify.setDescription(description);
        Coordinate coordinate = poiToModify.getCoordinate();
        coordinate.setLat(Double.parseDouble((String) bodyFrom.get("lat")));
        coordinate.setLon(Double.parseDouble((String) bodyFrom.get("lon")));
        this.coordinateRepository.save(coordinate);
        String street = (String) bodyFrom.get("street");
        Integer number = Integer.parseInt((String) bodyFrom.get("number"));
        Address address = poiToModify.getAddress();
        address.setNumber(number);
        address.setStreet(street);
        this.addressRepository.save(address);
        Contact contact = poiToModify.getContact();
        contact.setEmail((String) bodyFrom.get("email"));
        contact.setCellNumber((String) bodyFrom.get("phone"));
        contact.setFax((String) bodyFrom.get("fax"));
        contactRepository.save(contact);
        Integer timeToVisit = Integer.parseInt((String) bodyFrom.get("timeToVisit"));
        poiToModify.setTimeToVisit(timeToVisit);
        Double ticketPrice = Double.parseDouble((String) bodyFrom.get("price"));
        poiToModify.setTicketPrice(ticketPrice);
        TimeSlot timeSlot = poiToModify.getHours();
        clearTimeSlot(timeSlot);
        Collection<String> monday = (Collection<String>) bodyFrom.get("monday");
        Collection<String> tuesday = (Collection<String>) bodyFrom.get("tuesday");
        Collection<String> wednesday = (Collection<String>) bodyFrom.get("wednesday");
        Collection<String> thursday = (Collection<String>) bodyFrom.get("thursday");
        Collection<String> friday = (Collection<String>) bodyFrom.get("friday");
        Collection<String> saturday = (Collection<String>) bodyFrom.get("saturday");
        Collection<String> sunday = (Collection<String>) bodyFrom.get("sunday");
        if (!monday.isEmpty()) monday.forEach(s -> timeSlot.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> timeSlot.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> timeSlot.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> timeSlot.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> timeSlot.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> timeSlot.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> timeSlot.getSunday().add((LocalTime.parse(s))));
        timeSlotRepository.save(timeSlot);
        poiToModify.setHours(timeSlot);
        /*Collection<String> types = (Collection<String>) body.get("types");*/
        Collection<PoiType> poiTypes = ((Collection<String>) bodyFrom.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        poiToModify.setTypes(poiTypes);
        Collection<Map<String, Object>> poiTagRels = (Collection<Map<String, Object>>) bodyFrom.get("tags");
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String, Object> map : poiTagRels) {
            String tag = (String) map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if (!Objects.isNull(tagNode)) {
                if (tagNode.getIsBooleanType()) {
                    Boolean value = (Boolean) map.get("value");
                    poiTagRel.setBooleanValue(value);
                } else poiTagRel.setStringValue((String) map.get("value"));
            }
            values.add(poiTagRel);
        }
        poiToModify.setTagValues(values);
        this.pointOfIntRepository.save(poiToModify);
        return poiToModify;
    }

    public PointOfInterestNode createPoiFromRequest(PoiRequestNode requestFrom){
        PointOfInterestNode result = new PointOfInterestNode();
        result.setName(requestFrom.getName());
        result.setDescription(requestFrom.getDescription());
        result.setTimeToVisit(requestFrom.getTimeToVisit());
        result.setTicketPrice(requestFrom.getTicketPrice());
        result.setLink(requestFrom.getLink());
        result.setTypes(requestFrom.getTypes());
        result.getContributors().add(requestFrom.getUsername());
        TimeSlot requestHours = requestFrom.getHours();
        TimeSlot timeSlot = new TimeSlot(requestHours.getMonday(),requestHours.getTuesday(),
                requestHours.getWednesday(),requestHours.getThursday(),requestHours.getFriday(),
                requestHours.getSaturday(),requestHours.getSunday());
        this.timeSlotRepository.save(timeSlot);
        result.setHours(timeSlot);
        Collection<PoiTagRel> tagRelsToAdd = requestFrom.getTagValues();
        tagRelsToAdd.forEach(value->{
            PoiTagRel poiTagRel = new PoiTagRel(value.getTag());
            if(value.getTag().getIsBooleanType())
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
        Contact contactToAdd = new Contact(requestContact.getEmail(),requestContact.getCellNumber(),
                requestContact.getFax());
        this.contactRepository.save(contactToAdd);
        result.setContact(contactToAdd);
        this.pointOfIntRepository.save(result);
        return result;
    }
}
