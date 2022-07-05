package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
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
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

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
        /*Collection<String> types = (Collection<String>) body.get("types");*/
        Collection<PoiType> poiTypes = ((Collection<String>) body.get("types")).stream()
                .filter(a -> poiTypeRepository.findById(a).isPresent())
                .map(a -> poiTypeRepository.findById(a).get())
                .collect(Collectors.toList());
        /*Collection<PoiType> poiTypes = new ArrayList<>();
        for (String type: types) {
            if(poiTypeRepository.findById(type).isPresent()) {
                poiTypes.add(poiTypeRepository.findById(type).get());
            }
        }
         */
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
        poiRequestRepository.save(poiRequestNode);
        return ResponseEntity.ok(poiRequestNode);
    }




}
