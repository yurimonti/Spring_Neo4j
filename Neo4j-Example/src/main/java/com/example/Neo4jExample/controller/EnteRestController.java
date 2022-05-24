package com.example.Neo4jExample.controller;

import com.example.Neo4jExample.model.Category;
import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.model.PointOfInterest;
import com.example.Neo4jExample.repository.CategoryRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import com.example.Neo4jExample.service.EnteService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnteRestController {
    private final EnteService enteService;

    private final CategoryRepository categoryRepository;

    private final PointOfIntRepository pointOfIntRepository;

    private final EnteRepository enteRepository;


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

    @PostMapping("/ente/createPoi")
    public ResponseEntity createPOI(@RequestBody Map<String,Object> body){
        Ente ente1 = enteRepository.findByUsername((String) body.get("username"));
        String nomePoi = (String) body.get("poiName");
        String descrizione = (String) body.get("poiDescription");
        Long latLong = Long.parseLong((String) body.get("lat"));
        Long lonLong = Long.parseLong((String) body.get("lon"));
        Collection<String> categories = (Collection<String>) body.get("categories");
        Collection<Category> asdf = categories.stream().map(s -> categoryRepository.findAll().stream()
                .filter(category -> Objects.equals(category.getName(), s)).findFirst().get()).collect(Collectors.toList());
        //System.out.println(asdf);
        enteService.createPoi(ente1,nomePoi,descrizione,latLong,lonLong,asdf);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}

