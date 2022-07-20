package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.service.ProvaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DefaultController {
    private final ProvaService provaService;

    /**
     * Gets all the pois
     * @return a collection with all the pois
     */
    @GetMapping("/poi/all")
    public ResponseEntity<Collection<PointOfInterestNode>> getAllPois(@RequestParam String username){
        return ResponseEntity.ok(provaService.getAllPois(username));
    }

    /**
     * Gets all the cities
     * @return a collection with all the cities
     */
    @GetMapping("/city/all")
    public ResponseEntity<Collection<CityDTO>> getAllCities(){
        return ResponseEntity.ok(provaService.getAllCitiesAsDTOs());
    }

    /**
     * Gets all the categories
     * @return a collection with all the categories
     */
    @GetMapping("/category/all")
    public ResponseEntity<Collection<CategoryNode>> getCategoryAll(){
        return ResponseEntity.ok().body(provaService.getCategories());
    }

    /**
     * Gets all the poi types that have all the categories in the filter
     * @param filter the categories to use as a filter
     * @return all the poi types if the filter is empty else a collection of poi types filtered
     */
    @PostMapping("/poiType/all")
    public ResponseEntity<Collection<PoiType>> getPoiTypeFiltered(@RequestBody Collection<CategoryNode> filter){
        Collection<PoiType> result;
        if(filter.size()==0) result = provaService.getAllPoiTypes();
        else result = provaService.getPoiTypes(filter);
        return ResponseEntity.ok().body(result);
    }



    /**
     * prova per aggiungere un itinerario nel sistema
     * aggiunge tutti i poi di Camerino in un unico itinerario per prova
     * @return itinerario creato
     */
    /*
    @PostMapping("/addItinerary")
    public ResponseEntity<ItineraryNode> provaAddItinerary(){
        ItineraryNode itineraryNode = new ItineraryNode(cityRepository.findByName("Camerino"),
                pointOfIntRepository.findAll());
        itineraryRepository.save(itineraryNode);
        return ResponseEntity.ok(itineraryNode);
    }
    */



}
