package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ProvaService;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final ProvaService provaService;
    private final ContactRepository contactRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagRepository tagRepository;
    private final CityRepository cityRepository;
    private final ItineraryRepository itineraryRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final MySerializer<CityDTO> cityDTOMySerializer;


    @PostMapping("/addPoi")
    public ResponseEntity<PoiRequestNode> addPoi(@RequestBody Map<String,Object> body){
        //PointOfInterestNode poi = (PointOfInterestNode) body.get("originalPoi");
        String name = (String)body.get("name");
        String username = "An User";
        String description = (String)body.get("description");
        CityDTO cityDto = cityDTOMySerializer.deserialize(
                cityDTOMySerializer.serialize(body.get("city")),CityDTO.class);
        CityNode city = cityRepository.findById(cityDto.getId()).orElseThrow();
        Coordinate coordinate = provaService.createCoordsFromString(
                (String)body.get("lat"),(String)body.get("lon"));
        String street = (String) body.get("street");
        Integer number = Integer.parseInt((String) body.get("number"));
        Address address = provaService.createAddress(street,number);
        Contact contact = new Contact((String)body.get("email"),(String)body.get("phone"),
                (String)body.get("fax"));
        contactRepository.save(contact);
        Integer timeToVisit = Integer.parseInt((String) body.get("timeToVisit"));
        Double ticketPrice = Double.parseDouble((String) body.get("price"));
        TimeSlot timeSlot = new TimeSlot();
        Collection<String> monday = (Collection<String>) body.get("monday");
        Collection<String> tuesday = (Collection<String>) body.get("tuesday");
        Collection<String> wednesday = (Collection<String>) body.get("wednesday");
        Collection<String> thursday = (Collection<String>) body.get("thursday");
        Collection<String> friday = (Collection<String>) body.get("friday");
        Collection<String> saturday = (Collection<String>) body.get("saturday");
        Collection<String> sunday = (Collection<String>) body.get("sunday");
        if(!monday.isEmpty()) monday.forEach(s -> timeSlot.getMonday().add((LocalTime.parse(s))));
        if(!tuesday.isEmpty()) tuesday.forEach(s -> timeSlot.getTuesday().add((LocalTime.parse(s))));
        if(!wednesday.isEmpty()) wednesday.forEach(s -> timeSlot.getWednesday().add((LocalTime.parse(s))));
        if(!thursday.isEmpty()) thursday.forEach(s -> timeSlot.getThursday().add((LocalTime.parse(s))));
        if(!friday.isEmpty()) friday.forEach(s -> timeSlot.getFriday().add((LocalTime.parse(s))));
        if(!saturday.isEmpty()) saturday.forEach(s -> timeSlot.getSaturday().add((LocalTime.parse(s))));
        if(!sunday.isEmpty()) sunday.forEach(s -> timeSlot.getSunday().add((LocalTime.parse(s))));
        timeSlotRepository.save(timeSlot);
        /*Collection<String> types = (Collection<String>) body.get("types");*/
        Collection<PoiType> poiTypes = ((Collection<String>) body.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        Collection<Map<String,Object>> poiTagRels = (Collection<Map<String,Object>>) body.get("tags");
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String,Object> map : poiTagRels){
            String tag = (String)map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if(!Objects.isNull(tagNode)){
                if(tagNode.getIsBooleanType()){
                    Boolean value = Boolean.parseBoolean((String)map.get("value"));
                    poiTagRel.setBooleanValue(value);
                }
                else poiTagRel.setStringValue((String)map.get("value"));
            }
            values.add(poiTagRel);
        }
        PoiRequestNode poiRequestNode = new PoiRequestNode(name,description,city,coordinate,address,poiTypes);
        poiRequestNode.setUsername(username);
        poiRequestNode.setTagValues(values);
        poiRequestNode.setContact(contact);
        poiRequestNode.setTicketPrice(ticketPrice);
        poiRequestNode.setTimeSlot(timeSlot);
        poiRequestNode.setTimeToVisit(timeToVisit);
        poiRequestRepository.save(poiRequestNode);
        return ResponseEntity.ok(poiRequestNode);
    }




}
