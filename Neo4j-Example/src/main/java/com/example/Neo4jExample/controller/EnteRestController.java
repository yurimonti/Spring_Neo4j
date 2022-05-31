package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.CategoryRepository;
import com.example.Neo4jExample.repository.CityRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import com.example.Neo4jExample.service.EnteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnteRestController {
    private final EnteService enteService;

    private final CategoryRepository categoryRepository;

    private final PointOfIntRepository pointOfIntRepository;

    private final EnteRepository enteRepository;

    private final CityRepository cityRepository;

    private final StartRelRepo startRelRepo;


    @GetMapping("/poi/tags")
    public ResponseEntity<Collection<String>> getTagsFromPOI(@RequestBody Map<String,Object> body){
        //Optional<Category> category = categoryRepository.findById(categoryName);
        String name = (String) body.get("categoryName");
        Optional<Category> category = categoryRepository.findAll().stream()
                .filter(c -> Objects.equals(c.getName(), name)).findFirst();
        return category.map(value ->
                ResponseEntity.ok().body(value.getTagString()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

/*    @PostMapping("/ente/createPoi")
    public ResponseEntity createPOI(@RequestBody Map<String,Object> body){
        Ente ente1 = enteRepository.findByUsername((String) body.get("username"));
        String nomePoi = (String) body.get("poiName");
        String descrizione = (String) body.get("poiDescription");
        Double latLong = Double.parseDouble((String) body.get("lat"));
        Double lonLong = Double.parseDouble((String) body.get("lon"));
        Collection<String> categories = (Collection<String>) body.get("categories");
        Collection<Category> asdf = categories.stream().map(s -> categoryRepository.findAll().stream()
                .filter(category -> Objects.equals(category.getName(), s)).findFirst().get()).collect(Collectors.toList());
        //System.out.println(asdf);
        enteService.createPoi(ente1,nomePoi,descrizione,latLong,lonLong,asdf);
        return ResponseEntity.ok(HttpStatus.OK);
    }*/

    @PostMapping("/ente/createPoi")
    public ResponseEntity<HttpStatus> createPOI(@RequestBody Map<String,Object> body){
        Ente ente1 = enteRepository.findByUsername("ente1");
        String nomePoi = (String) body.get("name");
        String descrizione = (String) body.get("description");
        Double latLong = Double.parseDouble((String) body.get("lat"));
        Double lonLong = Double.parseDouble((String) body.get("lon"));
        PointOfInterest pointOfInterest = new PointOfInterest(nomePoi,descrizione,latLong,lonLong);
        City c = ente1.getCity();
        pointOfIntRepository.save(pointOfInterest);
        c.getPointOfInterests().add(pointOfInterest);
        cityRepository.save(c);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/prova/createPoi")
    public ResponseEntity<HttpStatus> provaCreatePoi(@RequestBody Map<String,?> body){
        String name = (String) body.get("name");
        String description = (String) body.get("description");
        Double lat = Double.parseDouble((String) body.get("lat"));
        Double lon = Double.parseDouble((String) body.get("lon"));
        PointOfInterest chiesaSanVenanzio = new PointOfInterest(name,description,lat,lon);
        chiesaSanVenanzio.getCategories().add(categoryRepository.findByName((String) body.get("categoryName")));
        pointOfIntRepository.save(chiesaSanVenanzio);
        Ente ente = enteRepository.findByUsername((String)body.get("username"));
        City city = ente.getCity();
        city.getPointOfInterests().add(chiesaSanVenanzio);
        cityRepository.save(city);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/category/all")
    public ResponseEntity<Collection<Category>> getCat(){
        return ResponseEntity.ok().body(categoryRepository.findAll());
    }

    @GetMapping("/prova/rel")
    public ResponseEntity<StartRel> getStartRel(){
        StartRel startRel = startRelRepo.findByName("nomeStart");
        return ResponseEntity.ok().body(startRel);
    }

}

