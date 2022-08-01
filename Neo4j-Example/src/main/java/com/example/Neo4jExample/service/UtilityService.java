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

@Service
@RequiredArgsConstructor
public class UtilityService {
    private final TagRepository tagRepository;
    private final AddressRepository addressRepository;
    private final CoordinateRepository coordinateRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final CityRepository cityRepository;

    public Coordinate createCoordsFromString(String lat, String lng){
        Coordinate result = new Coordinate(Double.parseDouble(lat),
                Double.parseDouble(lng));
        this.coordinateRepository.save(result);
        return result;
    }

    public Address createAddress(String street, Integer number){
        Address result = new Address(street,number);
        this.addressRepository.save(result);
        return result;
    }

    /**
     * fill a TimeSlot with params contained in a body request
     * @param toFill   TimeSlot to fill
     * @param bodyFrom body request
     * @return the filled TimeSlot
     */
    public TimeSlot getTimeSlotFromBody(TimeSlot toFill, Map<String, Object> bodyFrom) {
        Collection<String> monday = (Collection<String>) bodyFrom.get("monday");
        Collection<String> tuesday = (Collection<String>) bodyFrom.get("tuesday");
        Collection<String> wednesday = (Collection<String>) bodyFrom.get("wednesday");
        Collection<String> thursday = (Collection<String>) bodyFrom.get("thursday");
        Collection<String> friday = (Collection<String>) bodyFrom.get("friday");
        Collection<String> saturday = (Collection<String>) bodyFrom.get("saturday");
        Collection<String> sunday = (Collection<String>) bodyFrom.get("sunday");
        if (!monday.isEmpty()) monday.forEach(s -> toFill.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> toFill.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> toFill.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> toFill.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> toFill.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> toFill.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> toFill.getSunday().add((LocalTime.parse(s))));
        this.timeSlotRepository.save(toFill);
        return toFill;
    }

    public String getValueFromBody(String key, Map<String, Object> bodyFrom) {
        return (String) bodyFrom.get(key);
    }

    /**
     * create a PoiTagRel's Collection from a body request
     * @param from body request
     * @return a PoiTagRel's Collection from a body request
     */
    public Collection<PoiTagRel> createPoiTagRel(Collection<Map<String, Object>> from) {
        Collection<PoiTagRel> result = new ArrayList<>();
        for (Map<String, Object> map : from) {
            String tag = (String) map.get("tag");
            TagNode tagNode = this.tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if (!Objects.isNull(tagNode)) {
                if (tagNode.getIsBooleanType()) {
                    Boolean value = (Boolean) map.get("value");
                    poiTagRel.setBooleanValue(value);
                } else poiTagRel.setStringValue((String) map.get("value"));
            }
            result.add(poiTagRel);
        }
        return result;
    }

    /**
     * return the City of a certain PointOfInterestNode
     * @param poi to take city from
     * @return the City of the PointOfInterestNode
     */
    public CityNode getCityOfPoi(PointOfInterestNode poi) {
        return this.cityRepository.findAll().stream().filter(cityNode -> cityNode.getPointOfInterests().contains(poi))
                .findFirst().orElse(null);
    }
}
