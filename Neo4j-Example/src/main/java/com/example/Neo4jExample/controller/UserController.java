package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.ItineraryDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.*;
import com.example.Neo4jExample.service.util.MySerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PoiRequestService poiRequestService;
    private final CityRepository cityRepository;
    private final ItineraryService itineraryService;

    private final PoiService poiService;
    private final UtilityService utilityService;

    /**
     * create a Modify Request for a poi
     *
     * @param body http request
     * @return Modify Request
     */
    @PostMapping("/modifyPoi")
    public ResponseEntity<PoiRequestNode> modifyPoi(@RequestBody Map<String, Object> body) {
        PoiRequestNode result = this.poiRequestService.createModifyRequestFromBody(body);
        if (Objects.isNull(result)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }

    /**
     * create an Add Request of a poi
     *
     * @param body http request
     * @return Add Request
     */
    @PostMapping("/addPoi")
    public ResponseEntity<PoiRequestNode> addPoi(@RequestBody Map<String, Object> body) {
        PoiRequestNode result = this.poiRequestService.createAddRequestFromBody(body);
        if (Objects.isNull(result)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }

    /**
     * get all user's requests
     *
     * @param username of user
     * @return all requests
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
     * get all city's itinerary
     *
     * @param username who calls this api
     * @param cityId   id of city
     * @return all itineraries of this city
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

    @GetMapping("/itinerary/owner")
    public ResponseEntity<Collection<ItineraryDTO>> getOwnedItineries(@RequestParam String username) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        if (Objects.isNull(user)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user.getItineraries().stream().map(ItineraryDTO::new).toList());
    }

    @PostMapping("/itinerary")
    public HttpStatus createItinerary(@RequestParam String username,
                                      @RequestBody Map<String, Object> body) {
        ClassicUserNode user = this.userService.getClassicUserFromUser(username);
        if (Objects.isNull(user)) return FORBIDDEN;
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Collection<String> geoJsonList = (Collection<String>) body.get("geoJsonList");
        Collection<String> poiIds = (Collection<String>) body.get("poiIds");
        Collection<Long> ids = poiIds.stream().map(p -> Long.parseLong(p)).toList();
        Collection<PointOfInterestNode> pois = ids.stream().map(this.poiService::findPoiById).toList();
        Collection<CityNode> poiCities = pois.stream().map(this.utilityService::getCityOfPoi).distinct().toList();
        ItineraryNode result = this.itineraryService.createItinerary(name,description,pois, geoJsonList,
                user.getUser().getUsername(),false, poiCities.toArray(CityNode[]::new));
        if(Objects.isNull(result)) return HttpStatus.INTERNAL_SERVER_ERROR;
        this.userService.addItineraryToUser(user,result);
        return HttpStatus.CREATED;
    }

}
