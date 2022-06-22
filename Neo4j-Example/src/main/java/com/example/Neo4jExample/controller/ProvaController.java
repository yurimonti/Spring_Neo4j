package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ProvaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProvaController {
    private final ProvaService provaService;

    private final CoordinateRepository coordinateRepository;

    private final PointOfIntRepository pointOfIntRepository;

    private final PoiTypeRepository poiTypeRepository;

    private final EnteRepository enteRepository;
    private final AddressRepository addressRepository;

    private final TagRepository tagRepository;

    @GetMapping("/poi/all")
    public ResponseEntity<Collection<PointOfInterestNode>> getAllPois(){
        return ResponseEntity.ok(pointOfIntRepository.findAll());
    }

    @GetMapping("/category/all")
    public ResponseEntity<Collection<CategoryNode>> getCategoryAll(){
        return ResponseEntity.ok().body(provaService.getCategories());
    }

    @PostMapping("/poiType/all")
    public ResponseEntity<Collection<PoiType>> getPoiTypeFiltered(@RequestBody Collection<CategoryNode> filter){
        Collection<PoiType> result = provaService.getPoiTypes();
        if(filter.size()>0) result = provaService.getPoiTypes(filter);
        return ResponseEntity.ok().body(result);
    }

    //TODO: finire di completare il metodo
    @PostMapping("/ente/createPoi")
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
        Collection<Map<String,Object>> poiTagRels = (Collection<Map<String,java.lang.Object>>) body.get("tags");
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String,Object> map:poiTagRels){
            String tag = (String)map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if(!Objects.isNull(tagNode) && tagNode.getIsBooleanType()){
                Boolean value = Boolean.parseBoolean((String)map.get("value"));
                poiTagRel.setBooleanValue(value);
            }else poiTagRel.setStringValue((String)map.get("value"));
            values.add(poiTagRel);
        }
/*        Collection<PoiTagRel> poiTagRelsColl = new ArrayList<>();
        Collections.addAll(poiTagRelsColl, poiTagRels);*/
        PointOfInterestNode poi = provaService.createPoi(ente,name,description,address,coordinate,poiTypes,values);
        return Objects.isNull(poi) ? ResponseEntity.internalServerError().body(null) : ResponseEntity.ok(poi);
    }

    @PostMapping("/provaSer")
    public ResponseEntity<Collection<TagNode>> prova(@RequestBody Map<String,Object> body){
        Gson gson = new Gson();
        String json = gson.toJson(body.get("tags"));
        Type collectionType = new TypeToken<Collection<TagNode>>(){}.getType();
        Collection<TagNode> tags = gson.fromJson(json, collectionType);
        return ResponseEntity.ok(tags);
    }
}
