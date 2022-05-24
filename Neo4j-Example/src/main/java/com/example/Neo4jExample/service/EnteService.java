package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.Category;
import com.example.Neo4jExample.model.City;
import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.model.PointOfInterest;
import com.example.Neo4jExample.repository.CategoryRepository;
import com.example.Neo4jExample.repository.CityRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EnteService {
    private final PointOfIntRepository pointOfIntRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final EnteRepository enteRepository;


    public Collection<String> getTagStringFromCategory(Category category){
        return category.getTagString();
    }

    public void createPoi(Ente ente,String nome,String description,Long lat,Long lon){
        PointOfInterest poi = new PointOfInterest(nome,description,lat,lon);
        System.out.println(poi);
        City city = ente.getCity();
        pointOfIntRepository.save(poi);
        city.getPointOfInterests().add(poi);
        cityRepository.save(city);
        System.out.println(poi);
    }

    public void createProva(String nome,String description,Long lat,Long lon){
        PointOfInterest poi = new PointOfInterest(nome,description,lat,lon);
        pointOfIntRepository.save(poi);
    }
}
