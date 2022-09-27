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
    private final EnteService enteService;

    private final UtilityService utilityService;

    private Ente getEnteFromUsername(String username) {
        return this.userService.getEnteFromUser(username);
    }

    /**
     * Create a point of interest with a username of an ente and a body
     * @param username username of the ente who is trying to create the point of interest
     * @param body body of the http request that contains values
     * @return the new point of interest or an error if the point of interest is not found
     */
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
     * Get all the requests for points of interest of an ente given the username of the ente
     * @param username username of the ente
     * @return a collection of all the requests from the city of the ente as DTOs
     */
    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getRequestFromUsers(@RequestParam String username) {
        Ente ente = this.getEnteFromUsername(username);
        Collection<PoiRequestNode> result = this.poiRequestService.getFilteredRequests(poiRequestNode ->
                poiRequestNode.getCity().getId().equals(ente.getCity().getId()) &&
                        Objects.isNull(poiRequestNode.getAccepted()));
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }

    /**
     * Set the request with the given id to accepted or denied in uniformity to toSet.
     * @param toSet boolean value to set to the request
     * @param id the id of the request
     * @return noContent if the request is not found, otherwise ok
     */
    @PostMapping("/notifies")
    public ResponseEntity<PointOfInterestNode> setRequestTo(@RequestParam boolean toSet, @RequestParam Long id) {
        PoiRequestNode poiRequestNode = this.poiRequestService.findRequestById(id);
        if (Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        this.poiRequestService.changeStatusToRequest(poiRequestNode, toSet);
        if (toSet) {
            if (Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
                PointOfInterestNode poiToSet = this.poiService.createPoiFromRequest(poiRequestNode);
                this.poiRequestService.setPoiToRequest(poiRequestNode, poiToSet);
            } else {
                this.poiService.modifyPoiFromRequest(poiRequestNode);
                this.itineraryService.updateItinerariesByPoiModify(poiRequestNode.getPointOfInterestNode());
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Accept a modify request for a point of interest given its id
     * @param id the id of the request to accept
     * @param username username of the ente
     * @param body body of the http request that contains values
     * @return the point of interest modified
     */
    @PostMapping("/notifies/modify")
    public ResponseEntity<PointOfInterestNode> setRequestTo(@RequestParam Long id, @RequestParam String username,
                                                            @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PoiRequestNode poiRequestNode = this.poiRequestService.findRequestById(id);
        if (Objects.isNull(poiRequestNode)) return ResponseEntity.noContent().build();
        poiRequestNode.setAccepted(true);
        if (!Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
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
     * Modify a point of interest given its id
     * @param id the id of point of interest to modify
     * @param username username of the ente that want to modify
     * @param body body of the http request that contains values
     * @return the  point of interest modified
     */
    @PatchMapping("/poi")
    public ResponseEntity<PointOfInterestNode> modifyPoi(@RequestParam Long id, @RequestParam String username,
                                                         @RequestBody Map<String, Object> body) {
        Ente ente = this.getEnteFromUsername(username);
        PointOfInterestNode toModify = this.poiService.findPoiById(id);
        if (Objects.isNull(ente) || Objects.isNull(toModify)) return ResponseEntity.notFound().build();
        log.info("Poi before modifying {}", toModify.getName());
        this.poiService.modifyPoiFromBody(toModify, body);
        log.info("Poi after modifying {}", toModify.getName());
        this.itineraryService.updateItinerariesByPoiModify(toModify);
        return ResponseEntity.ok(toModify);
    }

    /**
     * Delete a point of interest given its id
     * @param username username of the ente that want to delete
     * @param id id of the point of interest to delete
     * @return FORBIDDEN if ente is not authorized to delete this poi;
     * NOT FOUND if poi not exists; OK if ended with success
     */
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
     * Create a new itinerary if only the ente's city is passed,
     * otherwise create a new itinerary request.
     * @param username username of the ente that wants to create the itinerary
     * @param body body of the http request that contains values
     * @return FORBIDDEN if the ente is not found, NOT FOUND if no geojson is passed,
     * NOT ACCEPTABLE if the ente's city is not present in the itinerary,
     * INTERNAL SERVER ERROR if the itinerary is not created or CREATED if ended with success
     */
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
        List<Long> ids = poiIds.stream().map(p -> Long.parseLong(p)).toList();
        Collection<PointOfInterestNode> pois = ids.stream().map(this.poiService::findPoiById).toList();
        log.info("list of pois to insert in itinerary : {}",pois.stream().map(PointOfInterestNode::getName).toList());
        //aggiunta controllo delle citta'
        Collection<CityNode> poiCities = ids.stream().map(this.utilityService::getCityOfPoi).distinct().toList();
        log.info("list of cities to insert in itinerary : {}",poiCities.stream().map(CityNode::getName).toList());
        if (!poiCities.stream().map(CityNode::getId).toList().contains(ente.getCity().getId()))
            return NOT_ACCEPTABLE;
        if (poiCities.size() > 1) {
            ItineraryRequestNode result = this.enteService.createItineraryRequest(ente,name,description,geoJsonList,
                    ids,poiCities);
            return Objects.isNull(result) ? INTERNAL_SERVER_ERROR : CREATED;
        }
        //fine controllo
        ItineraryNode result = this.enteService.createItinerary(ente,name,description,geoJsonList,ids);
        return Objects.isNull(result) ? INTERNAL_SERVER_ERROR : CREATED;
    }

    /**
     * Set the consensus of an itinerary request, if the ente already did, nothing is done
     * @param username username of the ente that wants to set the consensus
     * @param consensus true to accept, false to refuse
     * @param idRequest id of the itinerary
     * @return ACCEPTED if all the consensus of the itinerary have been set to true,
     * REJECTED if the consensus is false, PENDING if not everyone has set the consensus and none is false
     */
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
     * Get all the itineraries of the city of the ente
     * @param username username of the ente
     * @return a collection of all the itineraries of the city of the ente as DTOs
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
     * Delete an itinerary given its id if it's one of the itineraries of the city of the ente
     * @param itineraryId id of the itinerary to delete
     * @param username username of the ente
     * @return FORBIDDEN if the itinerary is not presente in the city of the ente,
     * OK if the itinerary has been deleted
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
     * Get all the requests for itineraries of an ente given the username of the ente
     * @param username username of the ente
     * @return a collection of all the requests for itineraries from the city of the ente as DTOs
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
