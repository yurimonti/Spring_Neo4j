package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.ItineraryDTO;
import com.example.Neo4jExample.dto.ItineraryRequestDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.*;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/ente")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class EnteController {
    private final PoiService poiService;
    private final PoiRequestService poiRequestService;
    private final ItineraryService itineraryService;
    private final UserService userService;

    private final UtilityService utilityService;

    private Ente getEnteFromUsername(String username) {
        return this.userService.getEnteFromUser(username);
    }

    //TODO:chiamare api "/poi/create"

    /**
     * create poi
     *
     * @param username of ente who creates poi
     * @param body     http request that contains values
     * @return new Poi
     */
    //SAFE!
    @PostMapping("/createPoi")
    public ResponseEntity<PointOfInterestNode> createPoi(@RequestParam String username, @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PointOfInterestNode poi = this.poiService.createPoiFromBody(body);
        log.info("created poi id: {}",poi.getId());
        CityNode city = ente.getCity();
        this.poiService.savePoiInACity(city, poi);
        return Objects.isNull(poi) ? ResponseEntity.internalServerError().build() : ResponseEntity.ok(poi);
    }

    /**
     * get all request for an Ente
     *
     * @param username of Ente
     * @return requests linked to a City's Poi
     */
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
        this.poiRequestService.changeStatusToRequest(poiRequestNode, toSet);
        //TODO: refactor
        if (toSet) {
            if (Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
                PointOfInterestNode poiToSet = this.poiService.createPoiFromRequest(poiRequestNode);
                this.poiRequestService.setPoiToRequest(poiRequestNode, poiToSet);
            } else {
                this.poiService.modifyPoiFromRequest(poiRequestNode);
                this.itineraryService.updateItinerariesByPoiModify(poiRequestNode.getPointOfInterestNode());
                /*this.provaService.changePoiFromRequest(poiRequestNode);*/
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * accept a Modify Request for a poi
     *
     * @param id       of a Request to accept
     * @param username of Ente
     * @param body     http request that contains values of modify
     * @return Poi modified
     */
    //TODO: modificato!! Guardare bene implementazione precedente se qualcosa va storto!!
    @PostMapping("/notifies/modify")
    public ResponseEntity<PointOfInterestNode> setRequestTo(@RequestParam Long id, @RequestParam String username,
                                                            @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PoiRequestNode poiRequestNode = this.poiRequestService.findRequestById(id);
        if (Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        poiRequestNode.setAccepted(true);
        if (!Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
            //poiResult = poiRequestNode.getPointOfInterestNode();
            log.info("Poi before modify request {}",poiRequestNode.getPointOfInterestNode().toString());
            this.poiService.modifyPoiFromBody(poiRequestNode.getPointOfInterestNode(), body);
            log.info("Poi after modify request {}",poiRequestNode.getPointOfInterestNode().toString());
            this.itineraryService.updateItinerariesByPoiModify(poiRequestNode.getPointOfInterestNode());
            this.poiRequestService.saveRequest(poiRequestNode);
        } else {
            PointOfInterestNode poiResult = this.poiService.createPoiFromBody(body);
            this.poiRequestService.setPoiToRequest(poiRequestNode, poiResult);
            this.poiService.savePoiInACity(ente.getCity(), poiResult);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * modify a poi
     *
     * @param id id of poi to modify
     * @param username of ente who calls this api
     * @param body     http request that contains values
     * @return Poi modified
     */
    //SAFE!
    @PatchMapping("/poi")
    public ResponseEntity<PointOfInterestNode> modifyPoi(@RequestParam Long id, @RequestParam String username,
                                                         @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PointOfInterestNode toModify = this.poiService.findPoiById(id);
        if (Objects.isNull(ente) || Objects.isNull(toModify)) return ResponseEntity.notFound().build();
        log.info("Poi before modifying {}", toModify);
        this.poiService.modifyPoiFromBody(toModify, body);
        log.info("Poi after modifying {}", toModify);
        this.itineraryService.updateItinerariesByPoiModify(toModify);
        return ResponseEntity.ok(toModify);
    }

    /**
     * delete a poi
     * @param username of ente who wants to delete poi
     * @param id of poi to delete
     * @return FORBIDDEN if ente is not authorized to delete this poi; NOT FOUND if poi not exists; OK if ended eith success
     */
    //SAFE!
    @DeleteMapping("/poi")
    public ResponseEntity<?> deletePoi(@RequestParam String username,@RequestParam Long id){
        Ente ente = this.getEnteFromUsername(username);
        if(Objects.isNull(ente)) return ResponseEntity.status(FORBIDDEN).build();
        PointOfInterestNode toDelete = this.poiService.findPoiById(id);
        if(!this.poiService.poiIsContainedInCity(toDelete,ente.getCity()))
            return ResponseEntity.status(FORBIDDEN).build();
        try{
            this.poiService.deletePoi(toDelete);
            log.info("toDelete: " + toDelete.toString());
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e.getClass()+" "+e.getMessage()+ " with id: {}",id );
            if(Objects.equals(e.getClass(),IllegalArgumentException.class))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * create a new ItineraryNode if request contains only ente's city;
     * create a new ItineraryNodeRequest otherwise.
     *
     * @param username of ente who wants to create itinerary
     * @param body     http request that contains values
     * @return HttpStatus of response call.
     */
    //TODO:da rendere safe se non lo e' !!
    @PostMapping("/itinerary")
    public HttpStatus createItinerary(@RequestParam String username,
                                      @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        if (Objects.isNull(ente)) return FORBIDDEN;
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Collection<String> geoJsonList = (Collection<String>) body.get("geoJsonList");
        if(geoJsonList.size() == 0) return NOT_FOUND;
        Collection<String> poiIds = (Collection<String>) body.get("poiIds");
        Collection<Long> ids = poiIds.stream().map(p -> Long.parseLong(p)).toList();
        Collection<PointOfInterestNode> pois = ids.stream().map(this.poiService::findPoiById).toList();
        log.info("list of pois to insert in itinerary : {}",pois.stream().map(PointOfInterestNode::getName).toList());
        //aggiunta controllo delle citta'
        Collection<CityNode> poiCities = pois.stream().map(PointOfInterestNode::getId)
                .map(this.utilityService::getCityOfPoi).distinct().toList();
        log.info("list of cities to insert in itinerary : {}",poiCities.stream().map(CityNode::getName).toList());
        if (!poiCities.contains(ente.getCity())) return NOT_ACCEPTABLE;
        if (poiCities.size() > 1) {
            ItineraryRequestNode result = this.itineraryService.createItineraryRequest(name,description,pois, geoJsonList,
                    ente.getUser().getUsername(), poiCities.toArray(CityNode[]::new));
            log.info("itinerary request created : {}",result.toString());
            return Objects.isNull(result) ? INTERNAL_SERVER_ERROR : CREATED;
        }
        //fine controllo
        ItineraryNode result = this.itineraryService.createItinerary(name,description,pois, geoJsonList,
                ente.getUser().getUsername(),true, ente.getCity());
        log.info("itinerary created : {}",result.toString());
        return Objects.isNull(result) ? INTERNAL_SERVER_ERROR : CREATED;
    }

    /**
     * set consensus to an itinerary request if the caller ente didn't before
     * @param username of ente who want to set consensus
     * @param consensus true if ente wants accept the itinerary
     * @param idRequest of itinerary to set consensus
     * @return status accepted if the others ente's set a positive consensus, rejected status otherwise
     */
    //TODO: da rendere safe se non lo e' !!
    @PatchMapping("/itinerary/consensus")
    public ResponseEntity<String> setConsensus(@RequestParam String username, @RequestParam boolean consensus,
                                               @RequestParam Long idRequest) {
        Ente ente = this.getEnteFromUsername(username);
        if (Objects.isNull(ente)) return ResponseEntity.status(FORBIDDEN).build();
        ItineraryRequestNode from = this.itineraryService.findRequestById(idRequest);
        if (Objects.isNull(from)) return ResponseEntity.notFound().build();
        this.itineraryService.updateConsensus(ente, from, consensus);
        if (Objects.isNull(from.getAccepted())) return ResponseEntity.ok("PENDING");
        if (!from.getAccepted()) return ResponseEntity.ok("REJECTED");
        return ResponseEntity.ok("ACCEPTED");
    }

    /**
     * get all itineraries of an Ente's City
     *
     * @param username of ente
     * @return all itineraries
     */
    @GetMapping("/itinerary")
    public ResponseEntity<Collection<ItineraryDTO>> getItineraries(@RequestParam String username) {
        Ente ente = this.getEnteFromUsername(username);
        if (Objects.isNull(ente)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.itineraryService.getItinerariesFiltered(i -> i.getCities().stream()
                .map(CityNode::getId)
                .anyMatch(c -> c.equals(ente.getCity().getId()))).stream().filter(ItineraryNode::getIsDefault)
                .map(ItineraryDTO::new).toList());
    }

    /**
     * Delete an Itinerary from the database
     * @param itineraryId id of ItineraryNode to delete
     * @param username of ente who calls api
     * @return status of the operation
     */
    @DeleteMapping("/itinerary")
    public ResponseEntity<HttpStatus> deleteItinerary(@RequestParam Long itineraryId, @RequestParam String username) {
        Ente ente = this.getEnteFromUsername(username);
        ItineraryNode toDelete = this.itineraryService.findItineraryById(itineraryId);
        if (Objects.isNull(toDelete) || Objects.isNull(ente)) return ResponseEntity.notFound().build();
        if (!toDelete.getCities().contains(ente.getCity()) || !toDelete.getIsDefault()) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        this.itineraryService.deleteItinerary(toDelete);
        log.info("itinerary deleted? --> {}",Objects.isNull(this.itineraryService.findItineraryById(itineraryId)));
        return ResponseEntity.ok().build();
    }

    /**
     * get all itinerary's requests
     * @param username of ente
     * @return all requests for an ente
     */
    @GetMapping("/itinerary/requests")
    public ResponseEntity<Collection<ItineraryRequestDTO>> getItineraryRequests(@RequestParam String username){
        Ente ente = this.getEnteFromUsername(username);
        if (Objects.isNull(ente)) return ResponseEntity.notFound().build();
        Collection<ItineraryRequestNode> requests = this.itineraryService.getItineraryRequests(r ->
                r.getCities().contains(ente.getCity()));
        return ResponseEntity.ok(requests.stream().map(ItineraryRequestDTO::new).toList());
    }
}
