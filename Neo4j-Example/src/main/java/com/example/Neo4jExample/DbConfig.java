package com.example.Neo4jExample;

import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.EnteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
    @Bean
    CommandLineRunner initDatabase(EnteRepository enteRepository, CityRepository cityRepository,
                                   PointOfIntRepository pointOfIntRepository, EnteService enteService,
                                   CategoryRepository categoryRepository, TagRepository tagRepository,
                                   PoiTypeRepository poiTypeRepository){
        return args -> {
            pointOfIntRepository.deleteAll();
            cityRepository.deleteAll();
            enteRepository.deleteAll();
            categoryRepository.deleteAll();
            tagRepository.deleteAll();
            poiTypeRepository.deleteAll();




        };
    }
}
