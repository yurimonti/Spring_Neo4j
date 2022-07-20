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
        String enteUsername = (String) body.get("username");
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
     *
     * @param body
     * @return
     */
    @PostMapping("/notifies/prova2")
    public ResponseEntity<PointOfInterestNode> modifyPoi(@RequestParam Long poiId,@RequestBody Map<String, Object> body){
        PointOfInterestNode point = this.provaService.getPoifromId(poiId);
        if(point == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(provaService.modifyPoi(point,body));
    }


    /**
     * Gets all the Poi request that concern an Ente from all users
     * @param username the name of the user Ente
     * @return a collection with the poi requests that concern an Ente as DTOs
     */
    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestsFromUsers(@RequestParam String username){
        return ResponseEntity.ok(provaService.getAllPoiRequestOfEnteAsDTOs(username));
    }

    /**
     * Sets PoiRequest to accepted or denied in uniformity to toSet
     * @param isAccepted true if accepted, false otherwise
     * @param idPoiRequest the id of the PoiRequest
     * @return the poi created/modified
     */
    @PostMapping("/notifies")
    public ResponseEntity<PointOfInterestNode> setPoiRequestStatus(@RequestParam boolean isAccepted, @RequestParam Long idPoiRequest){
        return ResponseEntity.ok(provaService.setPoiRequestStatus(isAccepted,idPoiRequest));
    }

    /**
     * Sets PoiRequest to accepted and modify the poi with information provided from the ente
     * @param idPoiRequest the id of the PoiRequest
     * @param body information provided from the ente
     * @return the poi modified
     */
    @PostMapping("/notifies/prova")
    public ResponseEntity<PointOfInterestNode> setPoiRequestWithModifications(@RequestParam Long idPoiRequest,
                                                                              @RequestParam String username,
                                                                              @RequestBody Map<String, Object> body){
        PointOfInterestNode point = provaService.getPoiFromRequest(idPoiRequest);
        if(point == null) return ResponseEntity.notFound().build();
        this.provaService.setNewPoiInCityFromUsername(point,username);
        return ResponseEntity.ok(provaService.modifyPoi(point,body));
    }




}
