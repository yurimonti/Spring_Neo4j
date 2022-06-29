package com.example.Neo4jExample;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
			Coordinate coordCitta = new Coordinate(43.139850, 13.069172);
			coordinateRepository.save(coordCitta);
			camerino.setCoordinate(coordCitta);
			cityRepository.save(camerino);
			enteProva.setCity(camerino);
			enteRepository.save(enteProva);

			TagNode tag1 = new TagNode("ingresso animali",true);
			TagNode tag2 = new TagNode("accessibilita disabili",true);
			TagNode tag3 = new TagNode("potabile",true);
			TagNode tag4 = new TagNode("costo",false);
			tagRepository.save(tag1);
			tagRepository.save(tag2);
			tagRepository.save(tag3);
			tagRepository.save(tag4);

			CategoryNode culturale =  new CategoryNode("Culturale");
			categoryRepository.save(culturale);
			CategoryNode spirituale =  new CategoryNode("Spirituale");
			categoryRepository.save(spirituale);
			CategoryNode architetturale =  new CategoryNode("Architetturale");
			categoryRepository.save(architetturale);
			CategoryNode gastronomia =  new CategoryNode("Gastronomia");
			categoryRepository.save(gastronomia);
			CategoryNode naturalistica =  new CategoryNode("Naturalistica");
			categoryRepository.save(naturalistica);
			CategoryNode fontanella =  new CategoryNode("Fontanella");
			categoryRepository.save(fontanella);
			CategoryNode zonaParcheggio =  new CategoryNode("ZonaParcheggio");
			categoryRepository.save(zonaParcheggio);
			CategoryNode mobilita =  new CategoryNode("Mobilita");
			categoryRepository.save(mobilita);



			PoiType chiesa = new PoiType("Chiesa");
			chiesa.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			chiesa.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(chiesa);

			PoiType monastero = new PoiType("Monastero");
			monastero.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			monastero.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(monastero);

			PoiType rocca = new PoiType("Rocca");
			rocca.getCategories().addAll(Arrays.asList(culturale,architetturale));
			rocca.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(rocca);

			PoiType palazzo = new PoiType("Palazzo");
			palazzo.getCategories().addAll(Arrays.asList(culturale,architetturale));
			palazzo.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(palazzo);

			PoiType tempio = new PoiType("Tempio");
			tempio.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			tempio.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(tempio);

			PoiType santuario = new PoiType("Santuario");
			santuario.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			santuario.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(santuario);

			PoiType cattedrale = new PoiType("Cattedrale");
			cattedrale.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			cattedrale.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(cattedrale);

			PoiType basilica = new PoiType("Basilica");
			basilica.getCategories().addAll(Arrays.asList(culturale,spirituale,architetturale));
			basilica.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(basilica);

			PoiType lago = new PoiType("Lago");
			lago.getCategories().add(naturalistica);
			lago.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(lago);

			PoiType biblioteca = new PoiType("Biblioteca");
			biblioteca.getCategories().addAll(Arrays.asList(culturale,architetturale));
			biblioteca.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(biblioteca);

			PoiType teatro = new PoiType("Teatro");
			teatro.getCategories().addAll(Arrays.asList(culturale,architetturale));
			teatro.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(teatro);

			PoiType mulino = new PoiType("Mulino");
			mulino.getCategories().addAll(Arrays.asList(culturale,architetturale));
			mulino.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(mulino);

			PoiType giardino = new PoiType("Giardino");
			giardino.getCategories().addAll(Arrays.asList(culturale,architetturale));
			giardino.getTags().addAll(Arrays.asList(tag1,tag2));
			poiTypeRepository.save(giardino);


			PoiType statua = new PoiType("Statua");
			statua.getCategories().add(culturale);
			poiTypeRepository.save(statua);

			PoiType museo = new PoiType("Museo");
			museo.getCategories().addAll(Arrays.asList(culturale,architetturale));
			museo.getTags().addAll(Arrays.asList(tag1,tag2,tag4));
			poiTypeRepository.save(museo);

			PoiType ristorante = new PoiType("Ristorante");
			ristorante.getCategories().add(gastronomia);
			poiTypeRepository.save(ristorante);

			PoiType parco = new PoiType("Parco");
			parco.getCategories().add(naturalistica);
			poiTypeRepository.save(parco);

			PoiType enoteca = new PoiType("Enoteca");
			enoteca.getCategories().addAll(Arrays.asList(gastronomia,naturalistica));
			poiTypeRepository.save(enoteca);

			PoiType bosco = new PoiType("Bosco");
			bosco.getCategories().add(naturalistica);
			poiTypeRepository.save(bosco);

			PoiType parcoGiochi = new PoiType("Parco Giochi");
			parcoGiochi.getCategories().add(naturalistica);
			poiTypeRepository.save(parcoGiochi);

			PoiType piazza = new PoiType("Piazza");
			piazza.getCategories().addAll(Arrays.asList(culturale,architetturale));
			poiTypeRepository.save(piazza);

			PoiType monumento = new PoiType("Monumento");
			monumento.getCategories().addAll(Arrays.asList(culturale,architetturale));
			poiTypeRepository.save(monumento);

			PoiType sostaCamper = new PoiType("Sosta Camper");
			sostaCamper.getCategories().add(zonaParcheggio);
			poiTypeRepository.save(sostaCamper);

			PoiType sostaMacchine = new PoiType("Sosta Macchine");
			sostaMacchine.getCategories().add(zonaParcheggio);
			poiTypeRepository.save(sostaMacchine);



			Coordinate pointProvaCoords;
			pointProvaCoords = new Coordinate(43.1392,13.0732);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Monastero di S. Chiara",camerino, monastero, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1483,13.102);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Chiesa e Convento dei Cappuccini di Renacavata",camerino, monastero, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.131,13.063);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Rocca Borgesca",camerino, rocca, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Rocca d'Ajello",camerino, rocca, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1103,13.1258);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Rocca Varano",camerino, rocca, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1357,13.0687);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Palazzo Ducale dei Da Varano",camerino, palazzo, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1373,13.0724);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Tempio dell'Annunziata",camerino, tempio, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1369,13.0671);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Area di sosta di Via Macario Muzio",camerino, sostaMacchine, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1393,13.0727);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Chiesa di S. Chiara",camerino, chiesa, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1358,13.0684);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Cattedrale di Santa Maria Annunziata",camerino, cattedrale, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1347,13.0647);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Chiesa di S. Filippo Neri",camerino, chiesa, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1319,13.0638);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Santuario di S. Maria in Via",camerino, santuario, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1377,13.0736);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Basilica di S. Venanzio Martire",camerino, basilica, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1468,13.1303);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Santuario Maria Madre della Misericordia",camerino, santuario, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.0911,13.1165);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Lago di Polverina",camerino, lago, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.136,13.0692);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Biblioteca comunale Valentiniana",camerino, biblioteca, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1658,13.0584);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Mulino Bottacchiari",camerino, mulino, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1362,13.07);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Orto Botanico Carmela Cortini Università di Camerino",camerino, museo, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1377,13.0713);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Polo museale di S. Domenico - Museo civico e archeologico – Pinacoteca civica Girolamo di Giovanni",camerino, museo, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1353,13.067);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Teatro Filippo Marchetti",camerino, teatro, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Giardini della Rocca d'Ajello",camerino, giardino, pointProvaCoords);

			pointProvaCoords = new Coordinate(43.1358,13.0698);
			createPoiProva(coordinateRepository, pointOfIntRepository, "Orto botanico di Camerino",camerino, giardino, pointProvaCoords);


			cityRepository.save(camerino);

		};
	}



	public void createPoiProva(CoordinateRepository coordinateRepository, PointOfIntRepository pointOfIntRepository
			,String nome, CityNode camerino, PoiType type, Coordinate coordinate) {
		PointOfInterestNode pointProva = new PointOfInterestNode(nome, "prova prova, 1,2,3, prova");
		coordinateRepository.save(coordinate);
		pointProva.setCoordinate(coordinate);
		pointProva.getTypes().add(type);
		for(TagNode tag : type.getTags()){
			PoiTagRel poiTagRel = new PoiTagRel(tag);
			poiTagRel.setBooleanValue(tag.getIsBooleanType());
			pointProva.getTagValues().add(poiTagRel);
		}
		pointOfIntRepository.save(pointProva);
		camerino.getPointOfInterests().add(pointProva);
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
