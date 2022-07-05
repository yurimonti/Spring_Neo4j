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
    private final ProvaService provaService;
    private final CoordinateRepository coordinateRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final EnteRepository enteRepository;
    private final AddressRepository addressRepository;
    private final TagRepository tagRepository;
    private final CityRepository cityRepository;
    private final ItineraryRepository itineraryRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final MySerializer<CityDTO> cityDTOMySerializer;


    //TODO: finire di completare il metodo
    @PostMapping("/createPoi")
    public ResponseEntity<PointOfInterestNode> createPoi(@RequestBody Map<String, Object> body){
        Ente ente = enteRepository.findByUsername("ente1");
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Double lat = Double.parseDouble((String)body.get("lat"));
        Double lon = Double.parseDouble((String)body.get("lon"));
        Coordinate coordinate = new Coordinate(lat,lon);
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
                    Boolean value = Boolean.parseBoolean((String)map.get("value"));
                    poiTagRel.setBooleanValue(value);
                }
                else poiTagRel.setStringValue((String)map.get("value"));
            }
            values.add(poiTagRel);
        }
        PointOfInterestNode poi = provaService.createPoi(ente,name,description,address,coordinate,poiTypes,values);
        return Objects.isNull(poi) ? ResponseEntity.internalServerError().body(null) : ResponseEntity.ok(poi);
    }


    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestFromUsers(@RequestParam String username){
        Ente ente = enteRepository.findByUsername(username);
        Collection<PoiRequestNode> result = poiRequestRepository.findAll().stream().filter(poiRequestNode ->
                poiRequestNode.getCity().getId().equals(ente.getCity().getId())).toList();
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }


}
