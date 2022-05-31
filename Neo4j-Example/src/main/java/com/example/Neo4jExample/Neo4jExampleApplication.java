package com.example.Neo4jExample;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.EnteService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
								   CategoryRepository categoryRepository, TagRepository tagRepository,
								   StartRelRepo startRelRepo){
		return args -> {
			pointOfIntRepository.deleteAll();
			cityRepository.deleteAll();
			enteRepository.deleteAll();
			categoryRepository.deleteAll();
			tagRepository.deleteAll();
			startRelRepo.deleteAll();
			City camerino = new City("Camerino");
			cityRepository.save(camerino);
			Ente ente1 = new Ente("ente1","ente1","ente1");
			ente1.setCity(camerino);
			enteRepository.save(ente1);
			Tag ingressoAnimali = new Tag("ingresso animali",true);
			Tag accessibilitaDisabili = new Tag("accessibilita disabili",true);
			Tag potabile = new Tag("potabile",true);
			Tag menuTag = new Tag("menu",false);
			tagRepository.save(ingressoAnimali);
			tagRepository.save(accessibilitaDisabili);
			tagRepository.save(potabile);
			tagRepository.save(menuTag);
			Category chiesa = new Category("Chiesa");
			Category biblioteca = new Category("Biblioteca");
			biblioteca.getTag().add(new CatHasTag(accessibilitaDisabili));
			biblioteca.getTag().add(new CatHasTag(ingressoAnimali));

			/*StartRel startRel = new StartRel("nomeStart");
			StartToTag startToTag = new StartToTag(potabile);
			startToTag.setBoolValue(true);
			startRel.getTags().add(startToTag);
			startRelRepo.save(startRel);*/
			/*CatHasTag ingAnimali = new CatHasTag(tag1);
			ingAnimali.setValue("false");*/
			/*CatHasTag menu = new CatHasTag(menuTag);
			Dish d1 = new Dish("pasta pomodoro",4.50);
			d1.setIngradients(List.of("pasta","pomodoro","olio oliva"));
			Dish d2 = new Dish("pasta bianco",3.00);
			d2.setIngradients(List.of("pasta","olio oliva"));
			FoodSection section = new FoodSection("primi");
			section.getDishes().addAll(List.of(d1,d2));
			Menu m = new Menu();
			m.setPrice(2);
			m.getFoodSections().add(section);
			menu.setValue(new ObjectMapper().writeValueAsString(m));
			//prova1.getTag().add(ingAnimali);
			biblioteca.getTag().add(menu);*/
			/*prova1.getTag().add(tag1);
			prova2.getTag().add(tag1);
			prova2.getTag().add(tag2);*/
			categoryRepository.save(biblioteca);
			chiesa.getTag().add(new CatHasTag(ingressoAnimali));
			categoryRepository.save(chiesa);
		};
    }
}
