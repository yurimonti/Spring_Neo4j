package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ProvaService;
import com.example.Neo4jExample.service.util.MySerializer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DefaultController {
    private final ProvaService provaService;
    private final CoordinateRepository coordinateRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final EnteRepository enteRepository;
    private final AddressRepository addressRepository;
    private final TagRepository tagRepository;
    private final CityRepository cityRepository;
    private final ItineraryRepository itineraryRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final MySerializer<CityDTO> cityDTOMySerializer;

    @GetMapping("/poi/all")
    public ResponseEntity<Collection<PointOfInterestNode>> getAllPois(){
        return ResponseEntity.ok(pointOfIntRepository.findAll());
    }

    @GetMapping("/city/all")
    public ResponseEntity<Collection<CityDTO>> getAllCities(){
        Collection<CityDTO> result = new ArrayList<>();
        Collection<CityNode> cities = cityRepository.findAll();
        cities.forEach(cityNode -> result.add(new CityDTO(cityNode.getId(),cityNode.getName())));
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
