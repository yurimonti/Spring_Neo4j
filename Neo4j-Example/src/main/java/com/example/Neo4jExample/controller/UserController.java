package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.ItineraryDTO;
import com.example.Neo4jExample.dto.ItineraryRequestDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PoiRequestService poiRequestService;
    private final CityRepository cityRepository;
    private final ItineraryService itineraryService;
    private final PoiService poiService;
    private final UtilityService utilityService;

    /**
     * Create a modify request for a poi
     * @param body body of the http request that contains values
     * @return the request if created, BAD REQUEST otherwise
     */
    @PostMapping("/modifyPoi")
    public ResponseEntity<PoiRequestNode> modifyPoi(@RequestBody Map<String, Object> body) {
        PoiRequestNode result;
        try{
            result = this.poiRequestService.createModifyRequestFromBody(body);
            log.info("modify request created successfully for poi : {}",result.getPointOfInterestNode().getName());
            return ResponseEntity.ok(result);
        }
        catch(Exception e){
            log.warn("error creating request cause poi is null : {} message: {}",
                    e.getClass().getSimpleName(),e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create an add request for a poi
     * @param body body of the http request that contains values
     * @return the request if created, BAD REQUEST otherwise
     */
    @PostMapping("/addPoi")
    public ResponseEntity<PoiRequestNode> addPoi(@RequestBody Map<String, Object> body) {
        PoiRequestNode result = this.poiRequestService.createAddRequestFromBody(body);
        if (Objects.isNull(result)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }

    /**
     * Get all the user's requests given the username of the user
     * @param username username of user
     * @return a collection with all the requests of the user as DTOs
     */
    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getUserRequests(@RequestParam String username) {
        UserNode user = this.userService.getUserByUsername(username);
        Collection<PoiRequestNode> result = this.poiRequestService.getFilteredRequests(poiRequestNode ->
                poiRequestNode.getUsername().equals(user.getUsername()));
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }

    /**
     * Get all the itineraries of a specific city
     * @param username username of the user
     * @param cityId id of the city
     * @return a collection of itineraries of a specific city as DTOs
     */
    @GetMapping("/itinerary")
    public ResponseEntity<Collection<ItineraryDTO>> getItineries(@RequestParam String username, @RequestParam Long cityId) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        if (this.cityRepository.findAll().stream().map(CityNode::getId).noneMatch(i -> Objects.equals(i, cityId)) ||
                Objects.isNull(user)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.itineraryService.getItinerariesFiltered(i -> i.getCities().stream()
                        .map(CityNode::getId)
                        .anyMatch(c -> c.equals(cityId))).stream().filter(ItineraryNode::getIsDefault)
                .map(ItineraryDTO::new).toList());
    }

    /**
     * Get all the itineraries of a user
     * @param username username of the user
     * @return NOT FOUND if the user is not found, a collection of itineraries as DTOs otherwise
     */
    @GetMapping("/itinerary/owner")
    public ResponseEntity<Collection<ItineraryDTO>> getOwnedItineries(@RequestParam String username) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        if (Objects.isNull(user)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user.getItineraries().stream().map(ItineraryDTO::new).toList());
    }

    /**
     * Create a request itinerary for the ente using an itinerary of the user
     * @param username username of the user
     * @param id id of the itinerary
     * @return the new itinerary request as a DTO, INTERNAL SERVER ERROR if the request is not created,
     * NOT FOUND if the user or the itinerary is not found
     */
    @PostMapping("/itinerary/owner")
    public ResponseEntity<ItineraryRequestDTO> createUserRequestItinerary(@RequestParam String username, @RequestParam Long id){
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        ItineraryNode toSet = this.itineraryService.findItineraryById(id);
        if (Objects.isNull(user) || Objects.isNull(toSet)) return ResponseEntity.notFound().build();
        Collection<PointOfInterestNode> pois = toSet.getPoints().stream().map(ItineraryRelPoi::getPoi).toList();
        ItineraryRequestNode result = this.itineraryService.createItineraryRequest(toSet.getName(),toSet.getDescription()
                ,pois,toSet.getGeoJsonList(),username, toSet.getCities().toArray(CityNode[]::new));
        if(Objects.isNull(result)) return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok(new ItineraryRequestDTO(result));
    }

    /**
     * Create an itinerary with a username of a user and a body
     * @param username username of the user who is trying to create the itinerary
     * @param body body of the http request that contains values
     * @return FORBIDDEN if the user is not found, INTERNAL SERVER ERROR if the itinerary is not created,
     * CREATED if the itinerary is created
     */
    @PostMapping("/itinerary")
    public HttpStatus createItinerary(@RequestParam String username,
                                      @RequestBody Map<String, Object> body) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        if (Objects.isNull(user)) return FORBIDDEN;
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Collection<String> geoJsonList = (Collection<String>) body.get("geoJsonList");
        Collection<String> poiIds = (Collection<String>) body.get("poiIds");
        Collection<Long> ids = poiIds.stream().map(Long::parseLong).toList();
        log.info("poiIds: {}", ids);
        Collection<PointOfInterestNode> pois = ids.stream().map(this.poiService::findPoiById).toList();
        log.info("pois: {}", pois);
        Collection<CityNode> poiCities = pois.stream().map(PointOfInterestNode::getId)
                .map(this.utilityService::getCityOfPoi).distinct().toList();
        log.info("list of cities: " + poiCities.stream().map(CityNode::getId).toList());
        System.out.println(poiCities.stream().map(CityDTO::new).toList());
        ItineraryNode result = this.itineraryService.createItinerary(name,description,pois, geoJsonList,
                user.getUser().getUsername(),false, poiCities.toArray(CityNode[]::new));
        if(Objects.isNull(result)) return HttpStatus.INTERNAL_SERVER_ERROR;
        this.userService.addItineraryToUser(user,result);
        return HttpStatus.CREATED;
    }

    /**
     * Delete an itinerary own by the user given its id
     * @param itineraryId id of the itinerary
     * @param username username of the user that own the itinerary
     * @return OK if the itinerary is deleted, FORBIDDEN if the user is not the owner or the itinerary is a default one,
     * NOT FOUND if the user or the itinerary is not found
     */
    @DeleteMapping("/itinerary")
    public ResponseEntity<HttpStatus> deleteItinerary(@RequestParam Long itineraryId, @RequestParam String username) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        ItineraryNode toDelete = this.itineraryService.findItineraryById(itineraryId);
        if (Objects.isNull(toDelete) || Objects.isNull(user)) return ResponseEntity.notFound().build();
        if (!Objects.equals(user.getUser().getUsername(), toDelete.getCreatedBy()) && (!toDelete.getIsDefault())) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        this.itineraryService.deleteItinerary(toDelete);
        return ResponseEntity.ok().build();
    }

}
