package com.example.Neo4jExample;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ProvaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.Console;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
public class Neo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jExampleApplication.class, args);
	}

	/**
	 * set Timer to update pois time open
	 */
	private void timerToUpdateTimeSlots(ProvaService provaService,Collection<PointOfInterestNode> pois) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				//TODO: inserire metodo per aggiornare se il poi è aggiornato
				//TODO: vedere se funziona
				provaService.updateOpenPois(pois,new Date());
			}
		},0,1000*60);
	}
	@Bean
	CommandLineRunner initDatabase(AddressRepository addressRepository, CategoryRepository categoryRepository,
								   CityRepository cityRepository,ContactRepository contactRepository,
								   CoordinateRepository coordinateRepository,DishNodeRepository dishNodeRepository,
								   EnteRepository enteRepository, FoodSectionNodeRepository foodSectionNodeRepository,
								   MenuNodeRepository menuNodeRepository,PointOfIntRepository pointOfIntRepository,
								   PoiTypeRepository poiTypeRepository, RestaurantPoiRepository restaurantPoiRepository,
								   TagRepository tagRepository, TimeSlotRepository timeSlotRepository,
								   ItineraryRepository itineraryRepo,PoiRequestRepository poiRequestRepository,
								   ProvaService provaService){
		return args -> {

			//cancella tutto prima di avviare il db
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
			itineraryRepo.deleteAll();
			poiRequestRepository.deleteAll();

			//creazione ente e citta'
			Ente enteProva = new Ente("ente1","ente1","ente1");
			CityNode camerino = new CityNode("Camerino");
			Coordinate coordCitta = new Coordinate(43.139850, 13.069172);
			coordinateRepository.save(coordCitta);
			camerino.setCoordinate(coordCitta);
			cityRepository.save(camerino);
			enteProva.setCity(camerino);
			enteRepository.save(enteProva);

			//Creazione TagNode
			TagNode tag1 = new TagNode("ingresso animali",true);
			TagNode tag2 = new TagNode("accessibilita disabili",true);
			TagNode tag3 = new TagNode("potabile",true);
			TagNode tag4 = new TagNode("costo",false);
			tagRepository.save(tag1);
			tagRepository.save(tag2);
			tagRepository.save(tag3);
			tagRepository.save(tag4);

			//creazione CategoryNode
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

			//Crezione PoiType con Tags
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

			//-------------------------Creazione Poi----------------------------

			//setup per TimeSlot
			Map<String,Collection<LocalTime>> orari = new HashMap<>();
			orari.put("Monday",new ArrayList<>());
			orari.put("Tuesday",new ArrayList<>());
			orari.put("Wednesday",new ArrayList<>());
			orari.put("Thursday",new ArrayList<>());
			orari.put("Friday",new ArrayList<>());
			orari.put("Saturday",new ArrayList<>());
			orari.put("Sunday",new ArrayList<>());
			orari.values().forEach(localTimes -> {
						localTimes.add(LocalTime.parse("08:00"));
						localTimes.add(LocalTime.parse("13:00"));
						localTimes.add(LocalTime.parse("14:00"));
						localTimes.add(LocalTime.parse("20:00"));
					}
			);
			TimeSlot timeSlot;
			//fine setup TimeSlot

			Coordinate pointProvaCoords;
			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1392,13.0732);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Monastero di S. Chiara",
					camerino, monastero, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1483,13.102);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Chiesa e Convento dei Cappuccini di Renacavata",camerino, monastero, pointProvaCoords,
					timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.131,13.063);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca Borgesca",camerino, rocca,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca d'Ajello",camerino, rocca,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1103,13.1258);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca Varano",camerino, rocca,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1357,13.0687);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Palazzo Ducale dei Da Varano",camerino,
					palazzo, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1373,13.0724);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Tempio dell'Annunziata",camerino, tempio,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1369,13.0671);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Area di sosta di Via Macario Muzio",
					camerino, sostaMacchine, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1393,13.0727);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Chiesa di S. Chiara",camerino, chiesa,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1358,13.0684);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Cattedrale di Santa Maria Annunziata",
					camerino, cattedrale, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1347,13.0647);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Chiesa di S. Filippo Neri",camerino,
					chiesa, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1319,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Santuario di S. Maria in Via",camerino,
					santuario, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1377,13.0736);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Basilica di S. Venanzio Martire",camerino,
					basilica, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1468,13.1303);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Santuario Maria Madre della Misericordia",
					camerino, santuario, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.0911,13.1165);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Lago di Polverina",camerino, lago,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.136,13.0692);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Biblioteca comunale Valentiniana",
					camerino, biblioteca, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1658,13.0584);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Mulino Bottacchiari",camerino, mulino,
					pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1362,13.07);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Orto Botanico Carmela Cortini Università di Camerino",camerino, museo, pointProvaCoords,
					timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1377,13.0713);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Polo museale di S. Domenico - Museo civico e archeologico – Pinacoteca civica Girolamo di Giovanni",
					camerino, museo, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1353,13.067);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Teatro Filippo Marchetti",camerino,
					teatro, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Giardini della Rocca d'Ajello",camerino,
					giardino, pointProvaCoords,timeSlot,timeSlotRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1358,13.0698);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Orto botanico di Camerino",camerino,
					giardino, pointProvaCoords,timeSlot,timeSlotRepository);


			cityRepository.save(camerino);

			//------------------- Fine Creazione Poi --------------------

			//creare una request aggiunta poi di prova
			Collection<PoiType> poiTypesRequest =  new ArrayList<>();
			poiTypesRequest.add(chiesa);

			pointProvaCoords = new Coordinate(43.13747,13.07314);
			coordinateRepository.save(pointProvaCoords);
			Address addressRequest = new Address("via",3);
			addressRepository.save(addressRequest);

			PoiRequestNode poiRequestNode = new PoiRequestNode("Fontanella Chiesa San Venanzio","asdf"
					,camerino,pointProvaCoords,addressRequest, poiTypesRequest);
			poiRequestNode.setUsername("Genoveffo");

			PoiTagRel poiTagRel1 = new PoiTagRel(tag1);
			poiTagRel1.setBooleanValue(true);
			PoiTagRel poiTagRel2 = new PoiTagRel(tag2);
			poiTagRel2.setBooleanValue(true);
			poiRequestNode.setTagValues(Arrays.asList(poiTagRel1,poiTagRel2));
			poiRequestRepository.save(poiRequestNode);

			//Timer che verifica e setta se un poi e' aperto o meno secondo l'istante corrente
			timerToUpdateTimeSlots(provaService,pointOfIntRepository.findAll());
		};
	}


/*	public void createPoiProva(CoordinateRepository coordinateRepository, PointOfIntRepository pointOfIntRepository
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
	}*/

	public void createPoiProva2(CoordinateRepository coordinateRepository, PointOfIntRepository pointOfIntRepository
			,String nome, CityNode camerino, PoiType type, Coordinate coordinate,TimeSlot hours,
								TimeSlotRepository timeSlotRepository){
		PointOfInterestNode pointProva = new PointOfInterestNode(nome, "prova prova, 1,2,3, prova");
		coordinateRepository.save(coordinate);
		pointProva.setCoordinate(coordinate);
		pointProva.getTypes().add(type);
		for(TagNode tag : type.getTags()){
			PoiTagRel poiTagRel = new PoiTagRel(tag);
			poiTagRel.setBooleanValue(tag.getIsBooleanType());
			pointProva.getTagValues().add(poiTagRel);
		}
		timeSlotRepository.save(hours);
		pointProva.setHours(hours);
		pointOfIntRepository.save(pointProva);
		camerino.getPointOfInterests().add(pointProva);
	}
}
