package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.service.ProvaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ente")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EnteController {
    private final ProvaService provaService;


    /**
     * Create a POI with parameters provided
     * @param body parameters of the POI
     * @return the new POI
     */
    @PostMapping("/createPoi")
    public ResponseEntity<PointOfInterestNode> createPoi(@RequestBody Map<String, Object> body){
        String enteUsername = "ente_camerino"; //TODO cambiare quando lo sistemiamo nel frontend
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Double lat = Double.parseDouble((String)body.get("lat"));
        Double lon = Double.parseDouble((String)body.get("lon"));
        Integer timeToVisit = Integer.parseInt((String) body.get("timeToVisit"));
        Double ticketPrice = Double.parseDouble((String) body.get("price"));
        String street = (String) body.get("street");
        Integer number = Integer.parseInt((String) body.get("number"));

        PointOfInterestNode newPoi = provaService.createBasicPoi(enteUsername,name,description,lat,lon,timeToVisit,ticketPrice,street,number);

        String email = (String)body.get("email");
        String phone = (String)body.get("phone");
        String fax = (String)body.get("fax");

        Collection<String> types = (Collection<String>) body.get("types");
        Collection<Map<String,Object>> poiTagRels = (Collection<Map<String,Object>>) body.get("tags");

        Collection<String> nameOfDays = new ArrayList<>(List.of("monday","tuesday","wednesday","thursday","friday","saturday","sunday"));
        Map<String,Collection<String>> mapSchedule = new HashMap<>();
        for(String day : nameOfDays) mapSchedule.put(day,(Collection<String>) body.get(day));

        newPoi = provaService.addInfoToNewPoi(newPoi,email,phone,fax,types,poiTagRels,mapSchedule);
        return ResponseEntity.ok(newPoi);
    }


    /**
     * Gets all the Poi request that concern an Ente from all users
     * @param username the name of the user Ente
     * @return a collection with the poi requests that concern an Ente as DTOs
     */
    @GetMapping("/notifies") //TODO controllare perchè probabilmente non funziona con la get
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestsFromUsers(@RequestParam String username){
        return ResponseEntity.ok(provaService.getAllPoiRequestOfEnteAsDTOs(username));
    }

    /**
     * Sets PoiRequest to accepted or denied in uniformity to toSet
     * @param status true if accepted, false otherwise
     * @param idPoiRequest of the PoiRequest
     * @return status of operation
     */
    @PostMapping("/notifies")
    public ResponseEntity<Object> setPoiRequestStatus(@RequestParam boolean status,@RequestParam Long idPoiRequest){
        return provaService.setPoiRequestStatus(status,idPoiRequest);
    }


}
