package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.*;
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

    private final PoiService poiService;
    private final PoiRequestService poiRequestService;
    private final ItineraryService itineraryService;
    private final UserService userService;
    private final UtilityService utilityService;

    private final ItineraryRepository itineraryRepository;

    private Ente getEnteFromUsername(String username){
        return this.userService.getEnteFromUser(username);
    }

    //TODO:chiamare api "/poi/create"
    @PostMapping("/createPoi")
    public ResponseEntity<PointOfInterestNode> createPoi(@RequestParam String username,@RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PointOfInterestNode poi = this.poiService.createPoiFromBody(body);
        CityNode city = ente.getCity();
        this.poiService.savePoiInACity(city,poi);
        return Objects.isNull(poi) ? ResponseEntity.internalServerError().build() : ResponseEntity.ok(poi);
    }


    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestFromUsers(@RequestParam String username) {
        Ente ente = this.getEnteFromUsername(username);
        Collection<PoiRequestNode> result = this.poiRequestService.getFilteredRequests(poiRequestNode ->
                poiRequestNode.getCity().getId().equals(ente.getCity().getId()) &&
                        Objects.isNull(poiRequestNode.getAccepted()));
/*        Collection<PoiRequestNode> result = this.poiRequestRepository.findAll().stream().filter(poiRequestNode ->
                        poiRequestNode.getCity().getId().equals(ente.getCity().getId()))
                .filter(poiRequestNode -> Objects.isNull(poiRequestNode.getAccepted())).toList();*/
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }

    /**
     * set Request to accepted or denied in uniformity to toSet
     *
     * @param toSet value of accepted to set
     * @param id    of Request
     * @return status of operation
     */
    @PostMapping("/notifies")
    public ResponseEntity<PointOfInterestNode> setRequestTo(@RequestParam boolean toSet, @RequestParam Long id) {
        PoiRequestNode poiRequestNode = this.poiRequestService.findRequestById(id);
        if (Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        this.poiRequestService.changeStatusToRequest(poiRequestNode,toSet);
        //TODO: refactor
        if (toSet) {
            if (Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
                PointOfInterestNode poiToSet = this.poiService.createPoiFromRequest(poiRequestNode);
                this.poiRequestService.setPoiToRequest(poiRequestNode,poiToSet);
            } else {
                this.poiService.modifyPoiFromRequest(poiRequestNode);
                /*this.provaService.changePoiFromRequest(poiRequestNode);*/
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifies/modify")
    public ResponseEntity<PointOfInterestNode> setRequestTo(@RequestParam Long id, @RequestParam String username,
                                                            @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PoiRequestNode poiRequestNode = this.poiRequestService.findRequestById(id);
        if (Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        poiRequestNode.setAccepted(true);
        PointOfInterestNode poiResult;
        if (!Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
            poiResult = poiRequestNode.getPointOfInterestNode();
            this.poiService.modifyPoiFromBody(poiResult, body);
        }
        else {
            poiResult = this.poiService.createPoiFromBody(body);
            CityNode city = ente.getCity();
            this.poiService.savePoiInACity(city,poiResult);
        }
        this.poiRequestService.setPoiToRequest(poiRequestNode,poiResult);
        return ResponseEntity.ok().body(poiRequestNode.getPointOfInterestNode());
    }

    @PatchMapping("/poi")
    public ResponseEntity<PointOfInterestNode> modifyPoi(@RequestParam Long poiId, @RequestParam String username,
                                                         @RequestBody Map<String, Object> body){
        Ente ente = this.getEnteFromUsername(username);
        PointOfInterestNode toModify = this.poiService.findPoiById(poiId);
        if(Objects.isNull(ente) || Objects.isNull(toModify)) return ResponseEntity.notFound().build();
        this.poiService.modifyPoiFromBody(toModify,body);
        return ResponseEntity.ok(toModify);
    }

    @PostMapping("/itinerary")
    public ResponseEntity<ItineraryNode> createItinerary(@RequestParam String username,
                                                         @RequestBody Map<String, Object> body){
        Ente ente = this.getEnteFromUsername(username);
        if(Objects.isNull(ente)) return ResponseEntity.notFound().build();
        String geojson = (String) body.get("geojson");
        Integer travelTime = Integer.parseInt((String) body.get("travelTime"));
        Collection<Long> poiIds = (Collection<Long>) body.get("poiIds");
        Collection<PointOfInterestNode> pois = poiIds.stream().map(this.poiService::findPoiById).toList();
        ItineraryNode result = this.itineraryService.createItinerary(ente.getCity(),pois,geojson,travelTime);
        this.itineraryRepository.save(result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<Collection<ItineraryNode>> getItineraries(@RequestParam String username){
        Ente ente = this.getEnteFromUsername(username);
        if(Objects.isNull(ente)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.itineraryRepository.findAll().stream()
                .filter(i -> Objects.equals(i.getCity().getId(),ente.getCity().getId())).toList());
    }

}
