package com.example.Neo4jExample;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.model2.Ente;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.EnteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
public class Neo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jExampleApplication.class, args);
	}


	private void initCategorisDB(CategoryRepository categoryRepository){
		Collection<Category> categories = new ArrayList<>();
		Category Gastronomia = new Category("Gastronomia");
		setTags(Gastronomia,"ingresso animali", "accessibilita disabili");
		categories.add(Gastronomia);
		Category Naturalistica = new Category("Naturalistica");
		setTags(Naturalistica,"ingresso animali", "accessibilita disabili","costo","selfie");
		categories.add(Naturalistica);
		Category Fontanella = new Category("Fontanella");
		setTags(Fontanella,"ingresso animali", "potabile");
		categories.add(Fontanella);
		Category ZonaParcheggio = new Category("ZonaParcheggio");
		setTags(ZonaParcheggio, "accessibilita disabili","costo");
		categories.add(ZonaParcheggio);
		Category Architetturale = new Category("Architetturale");
		setTags(Architetturale,"ingresso animali", "accessibilita disabili","costo","selfie");
		categories.add(Architetturale);
		Category Religioso = new Category("Religioso");
		setTags(Religioso,"ingresso animali", "accessibilita disabili","costo");
		categories.add(Religioso);
		Category Culturale = new Category("Culturale");
		setTags(Culturale,"ingresso animali", "accessibilita disabili","costo");
		categories.add(Culturale);
		Category Mobilita = new Category("Mobilita");
		setTags(Mobilita,"costo");
		categories.add(Mobilita);

		categoryRepository.saveAll(categories);
	}

	private void setTags(Category category, String ... args){
		for(String a : args){
			switch (a){
				case "ingresso animali" :
				case "potabile" :
				case "selfie" :
				case "accessibilita disabili" : category.getTagBool().add(a);break;
				default : category.getTagString().add(a);
			}
		}
	}

	@Bean
	CommandLineRunner initDatabase(EnteRepository enteRepository, CityRepository cityRepository,
								   PointOfIntRepository pointOfIntRepository, EnteService enteService,
								   CategoryRepository categoryRepository, TagRepository tagRepository, PoiTypeRepository macroCategoryRepository){
		return args -> {
			pointOfIntRepository.deleteAll();
			cityRepository.deleteAll();
			enteRepository.deleteAll();
			categoryRepository.deleteAll();
			tagRepository.deleteAll();
			City camerino = new City("Camerino");
			cityRepository.save(camerino);
			Ente ente1 = new Ente("ente1","ente1","ente1");
			ente1.setCity(camerino);
			enteRepository.save(ente1);
			//this.initCategorisDB(categoryRepository);

			Tag tag1 = new Tag("ingresso animali",true);
			Tag tag2 = new Tag("accessibilita disabili",false);
			Tag tag3 = new Tag("potabile",true);
			tagRepository.save(tag1);
			tagRepository.save(tag2);
			tagRepository.save(tag3);
			MacroCategory prova3 =  new MacroCategory("Culturale");
			MacroCategory prova4 =  new MacroCategory("Spirituale");
			MacroCategory prova5 =  new MacroCategory("Architetturale");
			Category prova1 = new Category("Chiesa");
			Category prova2 = new Category("Biblioteca");
			prova1.getMacroCategories().add(prova3);
			prova1.getMacroCategories().add(prova4);
			prova1.getMacroCategories().add(prova5);
			prova2.getMacroCategories().add(prova3);
			prova2.getMacroCategories().add(prova5);
			Object asdf = Boolean.TRUE;
			prova1.getTag().add(tag1);
			prova2.getTag().add(tag1);
			prova2.getTag().add(tag2);
			macroCategoryRepository.save(prova3);
			macroCategoryRepository.save(prova4);
			macroCategoryRepository.save(prova5);
			categoryRepository.save(prova1);
			categoryRepository.save(prova2);




			//this.provaNodiTag(categoryRepository,tagRepository);

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
