package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
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
@RequestMapping("/ente")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EnteController {
    private final ContactRepository contactRepository;
    private final ProvaService provaService;
    private final CoordinateRepository coordinateRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final UserNodeRepository userNodeRepository;
    private final AddressRepository addressRepository;
    private final TagRepository tagRepository;
    private final PoiRequestRepository poiRequestRepository;

    private final TimeSlotRepository timeSlotRepository;


    //TODO: finire di completare il metodo
    //TODO: refactoring
    @PostMapping("/createPoi")
    public ResponseEntity<PointOfInterestNode> createPoi(@RequestBody Map<String, Object> body){
        Ente ente = provaService.getEnteFromUser(userNodeRepository.findByUsername("ente_camerino"));
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Double lat = Double.parseDouble((String)body.get("lat"));
        Double lon = Double.parseDouble((String)body.get("lon"));
        Coordinate coordinate = new Coordinate(lat,lon);
        //TODO: da fare
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
        coordinateRepository.save(coordinate);
        String street = (String) body.get("street");
        Integer number = Integer.parseInt((String) body.get("number"));
        Address address = new Address(street,number);
        addressRepository.save(address);
        Collection<String> types = (Collection<String>) body.get("types");
        Collection<PoiType> poiTypes = new ArrayList<>();
        for (String type: types) {
            if(poiTypeRepository.findById(type).isPresent()) {
                poiTypes.add(poiTypeRepository.findById(type).get());
            }
        }
        Collection<Map<String,Object>> poiTagRels = (Collection<Map<String,Object>>) body.get("tags");
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String,Object> map:poiTagRels){
            String tag = (String)map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if(!Objects.isNull(tagNode)){
                if(tagNode.getIsBooleanType()){
                    Boolean value = (boolean)map.get("value");
                    poiTagRel.setBooleanValue(value);
                }
                else poiTagRel.setStringValue((String)map.get("value"));
            }
            values.add(poiTagRel);
        }
        PointOfInterestNode poi = provaService.createPoi(ente,name,description,address,coordinate,
                poiTypes,values,timeSlot,ticketPrice,contact,timeToVisit*60);
        return Objects.isNull(poi) ? ResponseEntity.internalServerError().body(null) : ResponseEntity.ok(poi);
    }


    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestFromUsers(@RequestParam String username){
        Ente ente = provaService.getEnteFromUser(userNodeRepository.findByUsername(username));
        Collection<PoiRequestNode> result = poiRequestRepository.findAll().stream().filter(poiRequestNode ->
                poiRequestNode.getCity().getId().equals(ente.getCity().getId()))
                .filter(poiRequestNode -> Objects.isNull(poiRequestNode.getAccepted())).toList();
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }

    /**
     * set Request to accepted or denied in uniformity to toSet
     * @param toSet value of accepted to set
     * @param id of Request
     * @return status of operation
     */
    @PostMapping("/notifies")
    public ResponseEntity<Object> setRequestTo(@RequestParam boolean toSet,@RequestParam Long id){
        PoiRequestNode poiRequestNode = null;
        if(poiRequestRepository.findById(id).isPresent()) poiRequestNode = poiRequestRepository.findById(id).get();
        if(Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        poiRequestNode.setAccepted(toSet);
        poiRequestRepository.save(poiRequestNode);
        if(toSet){
            if(Objects.isNull(poiRequestNode.getPointOfInterestNode())){
                PointOfInterestNode pointOfInterestNode = new PointOfInterestNode(poiRequestNode);
                timeSlotRepository.save(poiRequestNode.getTimeSlot());
                pointOfIntRepository.save(pointOfInterestNode);
            } else {
                provaService.changePoiFromRequest(poiRequestNode);
            }
        }
        return ResponseEntity.ok().build();
    }


}
