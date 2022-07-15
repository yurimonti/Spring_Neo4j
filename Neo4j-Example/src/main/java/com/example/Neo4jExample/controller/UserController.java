package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.service.ProvaService;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final ProvaService provaService;
    private final MySerializer<CityDTO> cityDTOMySerializer;


    /**
     * Create a Poi request with parameters provided
     * @param body parameters of the POI request
     * @return the new POI request
     */
    @PostMapping("/addPoi")
    public ResponseEntity<PoiRequestDTO> addPoi(@RequestBody Map<String,Object> body){
        String username = "An User"; //TODO cambiare quando lo sistemiamo nel frontend
        String name = (String)body.get("name");
        String description = (String)body.get("description");
        Double lat = Double.parseDouble((String)body.get("lat"));
        Double lon = Double.parseDouble((String)body.get("lon"));
        CityDTO cityDto = cityDTOMySerializer.deserialize(cityDTOMySerializer.serialize(body.get("city")),CityDTO.class);
        Integer timeToVisit = Integer.parseInt((String) body.get("timeToVisit"));
        Double ticketPrice = Double.parseDouble((String) body.get("price"));
        String street = (String) body.get("street");
        Integer number = Integer.parseInt((String) body.get("number"));

        PoiRequestNode newPoiRequest = provaService.createBasicPoiRequest(username,name,description,cityDto,lat,lon,timeToVisit,ticketPrice,street,number);

        String email = (String)body.get("email");
        String phone = (String)body.get("phone");
        String fax = (String)body.get("fax");

        Collection<String> daysOfWeek = new ArrayList<>(List.of("monday","tuesday","wednesday","thursday","friday","saturday","sunday"));
        Map<String,Collection<String>> mapSchedule = new HashMap<>();
        for(String day : daysOfWeek) mapSchedule.put(day,(Collection<String>) body.get(day));

        Collection<String> types = (Collection<String>) body.get("types");
        Collection<Map<String,Object>> poiTagRels = (Collection<Map<String,Object>>) body.get("tags");

        newPoiRequest = provaService.addInfoToNewPoiRequest(newPoiRequest,email,phone,fax,types,poiTagRels,mapSchedule);
        //return ResponseEntity.ok(newPoiRequest);
        PoiRequestDTO poiRequestDTO = new PoiRequestDTO(newPoiRequest);
        return ResponseEntity.ok(poiRequestDTO);
    }




}
