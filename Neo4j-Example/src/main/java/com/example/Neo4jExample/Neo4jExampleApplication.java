package com.example.Neo4jExample;

import com.example.Neo4jExample.model.Category;
import com.example.Neo4jExample.model.City;
import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.repository.CategoryRepository;
import com.example.Neo4jExample.repository.CityRepository;
import com.example.Neo4jExample.repository.EnteRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import com.example.Neo4jExample.service.EnteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Neo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jExampleApplication.class, args);
	}


	@Bean
    CommandLineRunner initDatabase(EnteRepository enteRepository, CityRepository cityRepository,
								   PointOfIntRepository pointOfIntRepository, EnteService enteService,
								   CategoryRepository categoryRepository){
        return args -> {
			pointOfIntRepository.deleteAll();
			cityRepository.deleteAll();
			enteRepository.deleteAll();
			categoryRepository.deleteAll();
			City camerino = new City("Camerino");
			cityRepository.save(camerino);
			Ente ente1 = new Ente("ente1","ente1","ente1");
			ente1.setCity(camerino);
			enteRepository.save(ente1);
			Category naturalistica = new Category("Naturalistica");
			naturalistica.getTagString().add("disabili");
			naturalistica.getTagString().add("cane");
			categoryRepository.save(naturalistica);
			/*Ente ente = new Ente("Marco","Montanari","marco.montanari");
			City city = new City("Camerino");
			ente.setCity(city);
			cityRepository.save(city);
			enteRepository.save(ente);
			enteService.createPOI(ente,"Università Inf",
					"desc",Long.getLong("5678"),Long.getLong("546734"));*/
        };
    }
}
