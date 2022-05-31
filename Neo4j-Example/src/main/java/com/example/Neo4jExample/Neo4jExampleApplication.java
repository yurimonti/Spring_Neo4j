package com.example.Neo4jExample;

import com.example.Neo4jExample.model2.*;
import com.example.Neo4jExample.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Neo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jExampleApplication.class, args);
	}
	@Bean
	CommandLineRunner initDatabase(AddressRepository addressRepository, CategoryRepository categoryRepository,
								   CityRepository cityRepository,ContactRepository contactRepository,
								   CoordinateRepository coordinateRepository,DishNodeRepository dishNodeRepository,
								   EnteRepository enteRepository, FoodSectionNodeRepository foodSectionNodeRepository,
								   MenuNodeRepository menuNodeRepository,PointOfIntRepository pointOfIntRepository,
								   PoiTypeRepository poiTypeRepository, RestaurantPoiRepository restaurantPoiRepository,
								   TagRepository tagRepository, TimeSlotRepository timeSlotRepository){
		return args -> {
			/*this.deleteRepositories(addressRepository,categoryRepository,cityRepository,contactRepository,
					coordinateRepository,dishNodeRepository,enteRepository,foodSectionNodeRepository,menuNodeRepository,
					pointOfIntRepository,poiTypeRepository,restaurantPoiRepository,tagRepository,timeSlotRepository);*/
			addressRepository.deleteAll();
			categoryRepository.deleteAll();
			cityRepository.deleteAll();
			contactRepository.deleteAll();
			coordinateRepository.deleteAll();
			dishNodeRepository.deleteAll();
			enteRepository.deleteAll();
			foodSectionNodeRepository.deleteAll();
			menuNodeRepository.deleteAll();
			pointOfIntRepository.deleteAll();
			poiTypeRepository.deleteAll();
			restaurantPoiRepository.deleteAll();
			tagRepository.deleteAll();
			timeSlotRepository.deleteAll();
			Ente enteProva = new Ente("ente1","ente1","ente1");
			CityNode camerino = new CityNode("Camerino");
			//camerino.setCoordinate(new Coordinate(43.139850, 13.069172));
			cityRepository.save(camerino);
			enteProva.setCity(camerino);
			enteRepository.save(enteProva);

			TagNode tag1 = new TagNode("ingresso animali",true);
			TagNode tag2 = new TagNode("accessibilita disabili",false);
			TagNode tag3 = new TagNode("potabile",true);
			tagRepository.save(tag1);
			tagRepository.save(tag2);
			tagRepository.save(tag3);

			CategoryNode culturale =  new CategoryNode("Culturale");
			CategoryNode spirituale =  new CategoryNode("Spirituale");
			CategoryNode architetturale =  new CategoryNode("Architetturale");
			CategoryNode gastronomia =  new CategoryNode("Gastronomia");
			CategoryNode naturalistica =  new CategoryNode("Naturalistica");
			CategoryNode fontanella =  new CategoryNode("Fontanella");
			CategoryNode zonaParcheggio =  new CategoryNode("ZonaParcheggio");
			CategoryNode mobilita =  new CategoryNode("Mobilita");
			categoryRepository.saveAll(Arrays.asList(culturale,spirituale,architetturale,gastronomia,naturalistica,
					fontanella,zonaParcheggio,mobilita));
			PoiType chiesa = new PoiType("Chiesa");
			chiesa.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			/*PoiType biblioteca = new PoiType("Biblioteca",culturale);
			PoiType statua = new PoiType("Statua",culturale);
			PoiType museo = new PoiType("Museo",architetturale,culturale);
			PoiType ristorante = new PoiType("Ristorante",gastronomia);
			PoiType parco = new PoiType("Parco",naturalistica);
			PoiType enoteca = new PoiType("Enoteca",gastronomia,naturalistica);
			PoiType bosco = new PoiType("Bosco",naturalistica);
			PoiType parcoGiochi = new PoiType("Parco Giochi",naturalistica);
			PoiType piazza = new PoiType("Piazza",culturale,architetturale);
			PoiType monumento = new PoiType("Monumento",architetturale,culturale);
			PoiType sostaCamper = new PoiType("Sosta Camper",zonaParcheggio);
			PoiType sostaMacchine = new PoiType("Sosta Macchine",zonaParcheggio);*/

			poiTypeRepository.save(chiesa);
			/*poiTypeRepository.saveAll(Arrays.asList(chiesa,biblioteca,statua,museo,ristorante,parco,enoteca,bosco
					,parco,parcoGiochi,piazza,monumento,sostaCamper,sostaMacchine));*/

		};
	}

	/*private void deleteRepositories(AddressRepository addressRepository, CategoryRepository categoryRepository,
									CityRepository cityRepository,ContactRepository contactRepository,
									CoordinateRepository coordinateRepository,DishNodeRepository dishNodeRepository,
									EnteRepository enteRepository, FoodSectionNodeRepository foodSectionNodeRepository,
									MenuNodeRepository menuNodeRepository,PointOfIntRepository pointOfIntRepository,
									PoiTypeRepository poiTypeRepository, RestaurantPoiRepository restaurantPoiRepository,
									TagRepository tagRepository, TimeSlotRepository timeSlotRepository){

		addressRepository.deleteAll();
		categoryRepository.deleteAll();
		cityRepository.deleteAll();
		contactRepository.deleteAll();
		coordinateRepository.deleteAll();
		dishNodeRepository.deleteAll();
		enteRepository.deleteAll();
		foodSectionNodeRepository.deleteAll();
		menuNodeRepository.deleteAll();
		pointOfIntRepository.deleteAll();
		poiTypeRepository.deleteAll();
		restaurantPoiRepository.deleteAll();
		tagRepository.deleteAll();
		timeSlotRepository.deleteAll();
	}*/

	/*
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

		//categoryRepository.saveAll(categories);
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
	*/
	/*
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
			prova1.getTag().add(tag1);
			prova2.getTag().add(tag1);
			prova2.getTag().add(tag2);
			macroCategoryRepository.save(prova3);
			macroCategoryRepository.save(prova4);
			macroCategoryRepository.save(prova5);
			categoryRepository.save(prova1);
			categoryRepository.save(prova2);




			this.provaNodiTag(categoryRepository,tagRepository);

			Ente ente = new Ente("Marco","Montanari","marco.montanari");
			City city = new City("Camerino");
			ente.setCity(city);
			cityRepository.save(city);
			enteRepository.save(ente);
			enteService.createPOI(ente,"Università Inf",
					"desc",Long.getLong("5678"),Long.getLong("546734"));
		};
    }*/
}
