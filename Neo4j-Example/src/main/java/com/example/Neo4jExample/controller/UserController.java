package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.ItineraryDTO;
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
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PoiRequestService poiRequestService;
    private final CityRepository cityRepository;
    private final ItineraryService itineraryService;

    @PostMapping("/modifyPoi")
    public ResponseEntity<PoiRequestNode> modifyPoi(@RequestBody Map<String, Object> body){
        PoiRequestNode result = this.poiRequestService.createModifyRequestFromBody(body);
        if(Objects.isNull(result))return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }
    @PostMapping("/addPoi")
    public ResponseEntity<PoiRequestNode> addPoi(@RequestBody Map<String, Object> body) {
        PoiRequestNode result = this.poiRequestService.createAddRequestFromBody(body);
        if(Objects.isNull(result))return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/notifies")
    public ResponseEntity<Collection<PoiRequestDTO>> getUserRequests(@RequestParam String username){
        UserNode user = this.userService.getUserByUsername(username);
        Collection<PoiRequestNode> result = this.poiRequestService.getFilteredRequests(poiRequestNode ->
                poiRequestNode.getUsername().equals(user.getUsername()));
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        result.forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));
        return ResponseEntity.ok(poiRequestDTOS);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<Collection<ItineraryDTO>> getItineries(@RequestParam String username,@RequestParam Long cityId){
        UserNode user = this.userService.getUserByUsername(username);
        if(this.cityRepository.findAll().stream().map(CityNode::getId).noneMatch(i -> Objects.equals(i, cityId)) ||
                Objects.isNull(user)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(this.itineraryService.getItinerariesFiltered(i -> i.getCities().stream()
                .map(CityNode::getId)
                .anyMatch(c -> c.equals(cityId))).stream().map(ItineraryDTO::new).toList());
    }
}
