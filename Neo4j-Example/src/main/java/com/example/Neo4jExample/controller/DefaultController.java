package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.ItineraryDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ItineraryService;
import com.example.Neo4jExample.service.ProvaService;
import com.example.Neo4jExample.service.util.MySerializer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DefaultController {
    private final ProvaService provaService;
    private final PointOfIntRepository pointOfIntRepository;
    private final CityRepository cityRepository;

    private final ItineraryService itineraryService;


    @GetMapping("/")
    public ResponseEntity<Date> provaDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        LocalTime localTime = LocalTime.parse("08:30");
        calendar.set(Calendar.HOUR_OF_DAY,localTime.getHour());
        calendar.set(Calendar.MINUTE,localTime.getMinute());
        calendar.set(Calendar.SECOND,localTime.getSecond());
        return ResponseEntity.ok(calendar.getTime());
    }

    //TODO:cancellare, poiche di prova
    @PostMapping("/provaGeojson")
    public ResponseEntity<String> sendGeojsonString(@RequestBody Map<String,Object> body){
        String geojson = (String)body.get("geojson");
        return ResponseEntity.ok(geojson);
    }

    @GetMapping("/poi/all")
    public ResponseEntity<Collection<PointOfInterestNode>> getAllPois(){
        return ResponseEntity.ok(pointOfIntRepository.findAll());
    }

    @GetMapping("/city/all")
    public ResponseEntity<Collection<CityDTO>> getAllCities(){
        Collection<CityNode> cities = cityRepository.findAll();
        Collection<CityDTO> result = new ArrayList<>(cities.stream().map(CityDTO::new).toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/all")
    public ResponseEntity<Collection<CategoryNode>> getCategoryAll(){
        return ResponseEntity.ok().body(provaService.getCategories());
    }

    @PostMapping("/poiType/all")
    public ResponseEntity<Collection<PoiType>> getPoiTypeFiltered(@RequestBody Collection<CategoryNode> filter){
        Collection<PoiType> result = provaService.getPoiTypes();
        if(filter.size()>0) result = provaService.getPoiTypes(filter);
        return ResponseEntity.ok().body(result);
    }

    //TODO:cancellare, poiche di prova
    @PostMapping("/provaInput")
    public ResponseEntity<Double> provaInput(@RequestBody Map<String, Object> body){
        Double result = Double.parseDouble((String)body.get("provaInput"));
        return ResponseEntity.ok().body(result);
    }

    //TODO:cancellare, poiche di prova
    @PostMapping("/provaInput2")
    public ResponseEntity<Double> provaInput2(@RequestBody Map<String, Object> body){
        Double result = (Double) body.get("provaInput");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/itinerary")
    public ResponseEntity<ItineraryDTO> getItineraryById(@RequestParam Long id){
        ItineraryNode it = this.itineraryService.findItineraryById(id);
        ItineraryDTO result = new ItineraryDTO();
        if(!Objects.isNull(it)) result = new ItineraryDTO(it);
        return ResponseEntity.ok(result);
    }

}
