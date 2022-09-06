package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.*;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DefaultController {

    private final ItineraryService itineraryService;
    private final PoiService poiService;
    private final PoiRequestService poiRequestService;
    private final UtilityService utilityService;

    /**
     * Get all the points of interest in the db
     * @return a collection of the points of interest in the db
     */
    @GetMapping("/poi/all")
    public ResponseEntity<Collection<PointOfInterestNode>> getAllPois(){
        return ResponseEntity.ok(utilityService.getAllPois());
    }

    /**
     * Gets all the cities in the db
     * @return a collection with all the cities as DTOs
     */
    @GetMapping("/city/all")
    public ResponseEntity<Collection<CityDTO>> getAllCities(){
        Collection<CityNode> cities = utilityService.getAllCities();
        Collection<CityDTO> result = new ArrayList<>(cities.stream().map(CityDTO::new).toList());
        return ResponseEntity.ok(result);
    }

    /**
     * Gets all the categories in the db
     * @return a collection with all the categories
     */
    @GetMapping("/category/all")
    public ResponseEntity<Collection<CategoryNode>> getCategoryAll(){
        return ResponseEntity.ok().body(utilityService.getCategories());
    }

    /**
     * Gets all the poi types that have all the categories in the filter
     * @param filter the categories to use as a filter
     * @return all the poi types if the filter is empty else a collection of poi types filtered
     */
    @PostMapping("/poiType/all")
    public ResponseEntity<Collection<PoiType>> getPoiTypeFiltered(@RequestBody Collection<CategoryNode> filter){
        Collection<PoiType> result = utilityService.getPoiTypes();
        if(filter.size()>0) result = utilityService.getPoiTypes(filter);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Get an itinerary given its id
     * @param id the id of the itinerary
     * @return the itinerary searched as a DTO or an empty ItineraryDTO if the itinerary is not found
     */
    @GetMapping("/itinerary")
    public ResponseEntity<ItineraryDTO> getItineraryById(@RequestParam Long id){
        ItineraryNode it = this.itineraryService.findItineraryById(id);
        ItineraryDTO result = new ItineraryDTO();
        if(!Objects.isNull(it)) result = new ItineraryDTO(it);
        return ResponseEntity.ok(result);
    }

    /**
     * Get an itinerary request given its id
     * @param id the id of the itinerary request
     * @return the itinerary request searched as a DTO
     * or an empty ItineraryRequestDTO if the itinerary request is not found
     */
    @GetMapping("/itinerary-request")
    public ResponseEntity<ItineraryRequestDTO> getItineraryRequestById(@RequestParam Long id){
        ItineraryRequestNode it = this.itineraryService.findRequestById(id);
        ItineraryRequestDTO result = new ItineraryRequestDTO();
        if(!Objects.isNull(it)) result = new ItineraryRequestDTO(it);
        return ResponseEntity.ok(result);
    }

    /**
     * Get a point of interest and the city given the id of the point of interest
     * @param id the id of the point of interest
     * @return a map with the city of the point of interest as DTO and the
     * point of interest as a DTO.
     * If any of the elements of the map is not found they return as an empty DTO
     */
    @GetMapping("/poi")
    public ResponseEntity<Map<String,Object>> getPoiById(@RequestParam Long id){
        PointOfInterestNode poi = this.poiService.findPoiById(id);
        PoiDTO result = new PoiDTO();
        if(!Objects.isNull(poi)) result = new PoiDTO(poi);
        CityNode city = this.utilityService.getAllCities().stream().filter(cityNode -> cityNode.getPointOfInterests()
                .contains(poi)).findFirst().orElse(null);
        CityDTO cityDto = new CityDTO();
        if(!Objects.isNull(city)) cityDto = new CityDTO(city);
        Map<String,Object> mapResult = new HashMap<>();
        mapResult.put("city", cityDto);
        mapResult.put("poi", result);
        return ResponseEntity.ok(mapResult);
    }

    /**
     * Get a point of interest request given its id
     * @param id the id of the point of interest request
     * @return the point of interest request as DTO or an empty PoiRequestDTO if the request is not found
     */
    @GetMapping("/request")
    public ResponseEntity<PoiRequestDTO> getRequestById(@RequestParam Long id){
        PoiRequestNode request = this.poiRequestService.findRequestById(id);
        PoiRequestDTO result = new PoiRequestDTO();
        if(!Objects.isNull(request)) result = new PoiRequestDTO(request);
        return ResponseEntity.ok(result);
    }

}
