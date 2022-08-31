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
    //TODO:cancellare
    /*private final PoiRequestRepository poiRequestRepository;
    private final ContactRepository contactRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final CoordinateRepository coordinateRepository;
    private final AddressRepository addressRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagRepository tagRepository;*/
    //fine cancellazione
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
   /* @PostMapping("/modifyPoi")
    public ResponseEntity<PoiRequestNode> modifyPoi(@RequestBody Map<String, Object> body) {
        List<PoiType> types = this.poiTypeRepository.findAll();
        types.forEach(t -> log.info("type {}", t.getName()));
        String poi = (String)body.get("poi");
        log.info("poiId: {}",poi);
        if(Objects.isNull(poi)) return ResponseEntity.notFound().build();
        PointOfInterestNode pointOfInterestNode = this.pointOfIntRepository.findById(Long.parseLong(poi)).orElse(null);
        if(Objects.isNull(pointOfInterestNode)) return ResponseEntity.notFound().build();
        CityNode city = this.cityRepository.findAll().stream()
                .filter(c -> c.getPointOfInterests().stream().map(PointOfInterestNode::getId).toList()
                        .contains(pointOfInterestNode.getId()))
                .findFirst().orElse(null);
        if(Objects.isNull(city)) return ResponseEntity.notFound().build();
        String username = (String)body.get("username");
        String name = (String)body.get("name");
        String description = (String)body.get("description");
        Coordinate coordinate = new Coordinate(Double.parseDouble((String)body.get("lat")),
                Double.parseDouble((String)body.get("lon")));
        this.coordinateRepository.save(coordinate);
        String street = (String)body.get("street");
        Integer number = Integer.parseInt((String)body.get("number"));
        Address address = new Address(street, number);
        this.addressRepository.save(address);
        Contact contact = new Contact((String)body.get("email"),(String)body.get("phone"),(String)body.get("fax"));
        this.contactRepository.save(contact);
        Double timeToVisit = Double.parseDouble((String)body.get("timeToVisit"));
        Double ticketPrice = Double.parseDouble((String)body.get("price"));
        Collection<String> monday = (Collection<String>) body.get("monday");
        Collection<String> tuesday = (Collection<String>) body.get("tuesday");
        Collection<String> wednesday = (Collection<String>) body.get("wednesday");
        Collection<String> thursday = (Collection<String>) body.get("thursday");
        Collection<String> friday = (Collection<String>) body.get("friday");
        Collection<String> saturday = (Collection<String>) body.get("saturday");
        Collection<String> sunday = (Collection<String>) body.get("sunday");
        TimeSlot timeSlot = new TimeSlot(monday.stream().map(LocalTime::parse).toList(),
                tuesday.stream().map(LocalTime::parse).toList(),wednesday.stream().map(LocalTime::parse).toList(),
                thursday.stream().map(LocalTime::parse).toList(),friday.stream().map(LocalTime::parse).toList(),
                saturday.stream().map(LocalTime::parse).toList(),sunday.stream().map(LocalTime::parse).toList());
        this.timeSlotRepository.save(timeSlot);
        *//*TimeSlot timeSlot = this.utilityService.getTimeSlotFromBody(new TimeSlot(),body);*//*
        Collection<PoiType> poiTypes = ((Collection<String>) body.get("types")).stream()
                .filter(a -> this.poiTypeRepository.findByName(a).isPresent())
                .map(a -> this.poiTypeRepository.findByName(a).get())
                .collect(Collectors.toList());
        log.info("types: {}",poiTypes);
        PoiRequestNode poiRequestNode = new PoiRequestNode(name,description,coordinate,timeSlot,timeToVisit,address,
                ticketPrice,username,poiTypes,contact);
        for (Map<String, Object> map : ((Collection<Map<String, Object>>) body.get("tags"))) {
            String tag = (String) map.get("tag");
            TagNode tagNode = this.tagRepository.findByName(tag).orElse(null);
            if (!Objects.isNull(tagNode)) {
                PoiTagRel poiTagRel = new PoiTagRel(tagNode);
                if (tagNode.getIsBooleanType()) {
                    poiTagRel.setBooleanValue((Boolean) map.get("value"));
                } else poiTagRel.setStringValue((String) map.get("value"));
                log.info("poiTagRel: {}",poiTagRel);
                poiRequestNode.getTagValues().add(poiTagRel);
            }
        }
        log.info("tag and Value of request {}: {}",poiRequestNode.getName(), poiRequestNode.getTagValues());
        log.info("Basic Request {}",poiRequestNode.getName());
        poiRequestNode.setPointOfInterestNode(pointOfInterestNode);
        poiRequestNode.setCity(city);
        this.poiRequestRepository.save(poiRequestNode);
        log.info("{}",poiRequestNode.getTagValues());
        return ResponseEntity.ok(poiRequestNode);
    }*/

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

    //FIXME: vedere perche se accetta non compaiono i pois
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
        Collection<CityNode> poiCities = pois.stream().map(PointOfInterestNode::getId).map(this.utilityService::getCityOfPoi).distinct().toList();
        System.out.println(poiCities.stream().map(CityDTO::new).toList());
        ItineraryNode result = this.itineraryService.createItinerary(name,description,pois, geoJsonList,
                user.getUser().getUsername(),false, poiCities.toArray(CityNode[]::new));
        if(Objects.isNull(result)) return HttpStatus.INTERNAL_SERVER_ERROR;
        this.userService.addItineraryToUser(user,result);
        return HttpStatus.CREATED;
    }

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
