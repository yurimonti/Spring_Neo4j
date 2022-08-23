package com.example.Neo4jExample;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.ProvaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class Neo4jExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jExampleApplication.class, args);
	}

	/**
	 * set Timer to update pois time open
	 */
	/*private void timerToUpdateTimeSlots(ProvaService provaService) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				provaService.updateOpenPois(new Date());
			}
		},0,1000*60);
	}*/
	@Bean
	CommandLineRunner initDatabase(AddressRepository addressRepository, CategoryRepository categoryRepository,
								   CityRepository cityRepository,ContactRepository contactRepository,
								   CoordinateRepository coordinateRepository,DishNodeRepository dishNodeRepository,
								   EnteRepository enteRepository, FoodSectionNodeRepository foodSectionNodeRepository,
								   MenuNodeRepository menuNodeRepository,PointOfIntRepository pointOfIntRepository,
								   PoiTypeRepository poiTypeRepository, RestaurantPoiRepository restaurantPoiRepository,
								   TagRepository tagRepository, TimeSlotRepository timeSlotRepository,
								   ItineraryRepository itineraryRepo,PoiRequestRepository poiRequestRepository,
								   ProvaService provaService,UserNodeRepository userNodeRepository,
								   UserRoleRepository userRoleRepository,ItineraryRequestRepository itineraryRequestRepository,
								   ClassicUserRepository classicUserRepository){
		return args -> {

			//prova user
			/*userNodeRepository.deleteAll();
			userRoleRepository.deleteAll();
			classicUserRepository.deleteAll();
			Collection<UserRole> roles = new ArrayList<>();
			UserRole userRole = new UserRole("user");
			UserRole enteRole = new UserRole("ente");
			UserRole commercianteRole = new UserRole("commerciante");
			UserRole adminRole = new UserRole("admin");
			Collections.addAll(roles,userRole,adminRole,commercianteRole,enteRole);
			userRoleRepository.saveAll(roles);
			//cancella tutto prima di avviare il db
			addressRepository.deleteAll();
			categoryRepository.deleteAll();
			cityRepository.deleteAll();
			contactRepository.deleteAll();
			coordinateRepository.deleteAll();
			dishNodeRepository.deleteAll();
			enteRepository.deleteAll();
			menuNodeRepository.deleteAll();
			pointOfIntRepository.deleteAll();
			poiTypeRepository.deleteAll();
			restaurantPoiRepository.deleteAll();
			tagRepository.deleteAll();
			timeSlotRepository.deleteAll();
			itineraryRepo.deleteAll();
			poiRequestRepository.deleteAll();
			itineraryRequestRepository.deleteAll();
			foodSectionNodeRepository.deleteAll();
			//creazione ente e citta'
			UserNode userEnteCamerino = new UserNode("mario","rossi","email","password",
					"ente_camerino",enteRole);
			userNodeRepository.save(userEnteCamerino);
			UserNode userEnteCastelRaimondo = new UserNode("carlo","verdi","email","password",
					"ente_castel_raimondo",enteRole);
			userNodeRepository.save(userEnteCastelRaimondo);
			UserNode user = new UserNode("marco","bianchi","email","password",
					"an_user",userRole);
			userNodeRepository.save(user);
			classicUserRepository.save(new ClassicUserNode(user));
			Ente enteCamerino = new Ente(userEnteCamerino);
			CityNode camerino = new CityNode("Camerino");
			Coordinate coordCamerino = new Coordinate(43.139850, 13.069172);
			coordinateRepository.save(coordCamerino);
			camerino.setCoordinate(coordCamerino);
			cityRepository.save(camerino);
			enteCamerino.setCity(camerino);
			enteRepository.save(enteCamerino);

			Ente enteCastelRaimondo = new Ente(userEnteCastelRaimondo);
			CityNode castelRaimondo = new CityNode("Castel Raimondo");
			Coordinate coordCastelRaimondo = new Coordinate(43.209100, 13.054600);
			coordinateRepository.save(coordCastelRaimondo);
			castelRaimondo.setCoordinate(coordCastelRaimondo);
			cityRepository.save(castelRaimondo);
			enteCastelRaimondo.setCity(castelRaimondo);
			enteRepository.save(enteCastelRaimondo);

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
					camerino, monastero, pointProvaCoords,orari,timeSlotRepository,contactRepository,addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1483,13.102);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Chiesa e Convento dei Cappuccini di Renacavata",camerino, monastero, pointProvaCoords,
					orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.131,13.063);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca Borgesca",camerino, rocca,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca d'Ajello",camerino, rocca,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1103,13.1258);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Rocca Varano",camerino, rocca,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1357,13.0687);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Palazzo Ducale dei Da Varano",camerino,
					palazzo, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1373,13.0724);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Tempio dell'Annunziata",camerino, tempio,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1369,13.0671);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Area di sosta di Via Macario Muzio",
					camerino, sostaMacchine, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1393,13.0727);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Chiesa di S. Chiara",camerino, chiesa,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1358,13.0684);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Cattedrale di Santa Maria Annunziata",
					camerino, cattedrale, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1347,13.0647);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Chiesa di S. Filippo Neri",camerino,
					chiesa, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1319,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Santuario di S. Maria in Via",camerino,
					santuario, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1377,13.0736);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Basilica di S. Venanzio Martire",camerino,
					basilica, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1468,13.1303);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Santuario Maria Madre della Misericordia",
					camerino, santuario, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.0911,13.1165);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Lago di Polverina",camerino, lago,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.136,13.0692);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Biblioteca comunale Valentiniana",
					camerino, biblioteca, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			timeSlot = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
					orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
			pointProvaCoords = new Coordinate(43.1658,13.0584);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Mulino Bottacchiari",camerino, mulino,
					pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			pointProvaCoords = new Coordinate(43.1362,13.07);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Orto Botanico Carmela Cortini Università di Camerino",camerino, museo, pointProvaCoords,
					orari, timeSlotRepository, contactRepository, addressRepository);

			pointProvaCoords = new Coordinate(43.1377,13.0713);
			createPoiProva2(coordinateRepository, pointOfIntRepository,
					"Polo museale di S. Domenico - Museo civico e archeologico – Pinacoteca civica Girolamo di Giovanni",
					camerino, museo, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			pointProvaCoords = new Coordinate(43.1353,13.067);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Teatro Filippo Marchetti",camerino,
					teatro, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			pointProvaCoords = new Coordinate(43.1885,13.0638);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Giardini della Rocca d'Ajello",camerino,
					giardino, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);

			pointProvaCoords = new Coordinate(43.1358,13.0698);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "Orto botanico di Camerino",camerino,
					giardino, pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);


			cityRepository.save(camerino);

			pointProvaCoords = new Coordinate(43.2104315,13.0526301);
			createPoiProva2(coordinateRepository, pointOfIntRepository, "PalaSport Castel Raimondo",castelRaimondo,
					palazzo,pointProvaCoords, orari, timeSlotRepository, contactRepository, addressRepository);
			cityRepository.save(castelRaimondo);*/

			//------------------- Fine Creazione Poi --------------------

			//------------------- Inizio Creazione Itinerario -----------


			//------------------- Fine Creazione Itinerario -------------

			//creare una request aggiunta poi di prova
			/*Collection<PoiType> poiTypesRequest =  new ArrayList<>();
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
			poiRequestRepository.save(poiRequestNode);*/

			//Timer che verifica e setta se un poi e' aperto o meno secondo l'istante corrente
			//this.timerToUpdateTimeSlots(provaService);
		};
	}

	public void createPoiProva2(CoordinateRepository coordinateRepository, PointOfIntRepository pointOfIntRepository,
								String nome, CityNode camerino, PoiType type, Coordinate coordinate,
								Map<String, Collection<LocalTime>> orari, TimeSlotRepository timeSlotRepository,
								ContactRepository contactRepository, AddressRepository addressRepository){

		PointOfInterestNode pointProva = new PointOfInterestNode(nome, "prova prova, 1,2,3, prova");
		coordinateRepository.save(coordinate);
		pointProva.setCoordinate(coordinate);

		Contact contact = new Contact("asdf@asdf.com","1234567890","nonSoComeSiScriveUnFax");
		contactRepository.save(contact);
		pointProva.setContact(contact);

		Address address = new Address("via Qualcosa",6);
		addressRepository.save(address);
		pointProva.setAddress(address);

		pointProva.setTicketPrice(0.0);
		pointProva.setTimeToVisit(20.0);


		pointProva.getTypes().add(type);
		for(TagNode tag : type.getTags()){
			PoiTagRel poiTagRel = new PoiTagRel(tag);
			poiTagRel.setBooleanValue(tag.getIsBooleanType());
			pointProva.getTagValues().add(poiTagRel);
		}

		TimeSlot hours = new TimeSlot(orari.get("Monday"),orari.get("Tuesday"),orari.get("Wednesday"),
				orari.get("Thursday"),orari.get("Friday"),orari.get("Saturday"),orari.get("Sunday"));
		timeSlotRepository.save(hours);
		pointProva.setHours(hours);
		pointOfIntRepository.save(pointProva);
		camerino.getPointOfInterests().add(pointProva);
	}
}
