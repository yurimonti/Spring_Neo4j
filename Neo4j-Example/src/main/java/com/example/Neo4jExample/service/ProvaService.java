package com.example.Neo4jExample.service;

import com.example.Neo4jExample.dto.CityDTO;
import com.example.Neo4jExample.dto.PoiRequestDTO;
import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProvaService {
    private final PointOfIntRepository pointOfIntRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CoordinateRepository coordinateRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagRepository tagRepository;
    private final UserNodeRepository userNodeRepository;
    private final PoiRequestRepository poiRequestRepository;
    private final EnteRepository enteRepository;

    /**
     * Gets all the pois from db
     *
     * @return a collection with all the pois
     */
    public Collection<PointOfInterestNode> getAllPois() {
        return pointOfIntRepository.findAll();
    }

    /**
     * Gets all the cities from db
     *
     * @return a collection with all the cities
     */
    public Collection<CityNode> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Gets all the cities as DTOs
     *
     * @return a collection with all the cities as DTOs
     */
    public Collection<CityDTO> getAllCitiesAsDTOs() {
        Collection<CityNode> cities = this.getAllCities();
        return new ArrayList<>(cities.stream().map(CityDTO::new).toList());
    }

    /**
     * Gets the Ente linked to a user
     *
     * @param user the user
     * @return Ente if the user is linked to an Ente or else throw
     */
    public Ente getEnteFromUser(UserNode user) {
        return enteRepository.findAll().stream().filter(ente -> ente.getUser().equals(user)).findFirst().orElse(null);
    }

    /**
     * Gets all the poi types
     *
     * @return a collection with all the poi types
     */
    public Collection<PoiType> getAllPoiTypes() {
        return poiTypeRepository.findAll();
    }

    /**
     * Gets all the poi types that have all the categories in the filter
     *
     * @param categoriesFilter the categories to use as a filter
     * @return a collection of poi types filtered
     */
    public Collection<PoiType> getPoiTypes(Collection<CategoryNode> categoriesFilter) {
        return getAllPoiTypes().stream().filter(t -> t.getCategories().containsAll(categoriesFilter)).toList();
    }

    /**
     * Gets all the categories
     *
     * @return a collection with all the categories
     */
    public Collection<CategoryNode> getCategories() {
        return categoryRepository.findAll();
    }


    /**
     * Check and update if all pois are open in a certain date
     *
     * @param pois to check
     * @param date to validating the check
     */
    public void updateOpenPois(Collection<PointOfInterestNode> pois, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        pois.forEach(pointOfInterestNode -> updateOpenPoi(pointOfInterestNode, calendar));
    }


    private void updateOpenPoi(PointOfInterestNode poi, Calendar calendar) {
        ArrayList<LocalTime> dailySchedule = new ArrayList<>(this.gatDailySchedule(poi.getHours(), calendar));
        boolean isOpen = false;
        LocalTime timeNow = LocalTime.ofInstant(calendar.toInstant(), TimeZone.getDefault().toZoneId());
        int scheduleSize = dailySchedule.size();
        if (scheduleSize == 2) {
            if (this.scheduleControl(dailySchedule.get(0), dailySchedule.get(1), timeNow)) isOpen = true;
        } else if (scheduleSize == 4) {
            if (this.scheduleControl(dailySchedule.get(0), dailySchedule.get(1), timeNow) ||
                    this.scheduleControl(dailySchedule.get(2), dailySchedule.get(3), timeNow)) isOpen = true;
        } else if(scheduleSize%2 == 1) throw new IllegalArgumentException("Non dovresti essere qui");

        poi.getHours().setIsOpen(isOpen);
        timeSlotRepository.save(poi.getHours());
        pointOfIntRepository.save(poi);
    }

    private boolean scheduleControl(LocalTime primo, LocalTime secondo, LocalTime timeNow) {
        return primo.isBefore(timeNow) && secondo.isAfter(timeNow);
    }

    private Collection<LocalTime> gatDailySchedule(TimeSlot hours, Calendar calendar) {
        return switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> hours.getMonday();
            case Calendar.TUESDAY -> hours.getTuesday();
            case Calendar.WEDNESDAY -> hours.getWednesday();
            case Calendar.THURSDAY -> hours.getThursday();
            case Calendar.FRIDAY -> hours.getFriday();
            case Calendar.SATURDAY -> hours.getSaturday();
            case Calendar.SUNDAY -> hours.getSunday();
            default -> new ArrayList<>();
        };
    }


    /**
     * Sets PoiRequest to accepted or denied in uniformity to toSet
     *
     * @param isAccepted   true if accepted, false otherwise
     * @param idPoiRequest of the PoiRequest
     * @return status of operation
     */
    public PointOfInterestNode setPoiRequestStatus(boolean isAccepted, Long idPoiRequest) {
        PoiRequestNode poiRequestNode;
        if (poiRequestRepository.findById(idPoiRequest).isPresent()) {
            poiRequestNode = poiRequestRepository.findById(idPoiRequest).get();
        } else return null;

        poiRequestNode.setAccepted(isAccepted);
        poiRequestRepository.save(poiRequestNode);
        PointOfInterestNode result = null;
        if (isAccepted) {
            if (Objects.isNull(poiRequestNode.getPointOfInterestNode())) {
                result = this.createPoiFromRequest(poiRequestNode);
            } else result = this.changePoiFromRequest(poiRequestNode);
        }
        return result;
    }

    private PointOfInterestNode createPoiFromRequest(PoiRequestNode poiRequestNode) {
        PointOfInterestNode result = new PointOfInterestNode(poiRequestNode);
        result.setHours(new TimeSlot(poiRequestNode.getHours()));
        timeSlotRepository.save(result.getHours());
        result.setCoordinate(new Coordinate(poiRequestNode.getCoordinate()));
        coordinateRepository.save(result.getCoordinate());
        result.setContact(new Contact(poiRequestNode.getContact()));
        contactRepository.save(result.getContact());
        result.setAddress(new Address(poiRequestNode.getAddress()));
        addressRepository.save(result.getAddress());
        Collection<PoiTagRel> poiTagRels = poiRequestNode.getTagValues().stream().map(PoiTagRel::new).toList();
        result.setTagValues(poiTagRels);

        pointOfIntRepository.save(result);
        poiRequestNode.getCity().getPointOfInterests().add(result);
        cityRepository.save(poiRequestNode.getCity());
        return result;

    }


    private PointOfInterestNode changePoiFromRequest(PoiRequestNode poiRequestNode) {
        PointOfInterestNode result = poiRequestNode.getPointOfInterestNode();

        result.setHours(new TimeSlot(poiRequestNode.getHours()));
        timeSlotRepository.save(result.getHours());

        result.setCoordinate(new Coordinate(poiRequestNode.getCoordinate()));
        coordinateRepository.save(result.getCoordinate());

        result.setTicketPrice(poiRequestNode.getTicketPrice());

        result.setContact(new Contact(poiRequestNode.getContact()));
        contactRepository.save(result.getContact());

        result.setName(poiRequestNode.getName());
        result.setDescription(poiRequestNode.getDescription());
        result.setTimeToVisit(poiRequestNode.getTimeToVisit());

        result.setAddress(new Address(poiRequestNode.getAddress()));
        addressRepository.save(result.getAddress());

        result.getContributors().add(poiRequestNode.getUsername());
        result.setLink(poiRequestNode.getLink());
        result.setTypes(poiRequestNode.getTypes());

        Collection<PoiTagRel> poiTagRels = poiRequestNode.getTagValues().stream().map(PoiTagRel::new).toList();
        result.setTagValues(poiTagRels);

        poiRequestRepository.save(poiRequestNode);
        pointOfIntRepository.save(result);
        return result;
    }


    /**
     * Gets all the Poi requests for an ente as DTO
     *
     * @param username username of the user ente
     * @return a collection of PoiRequestDTOs
     */
    public Collection<PoiRequestDTO> getAllPoiRequestOfEnteAsDTOs(String username) {
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();
        Long idCityEnte = this.getEnteFromUser(userNodeRepository.findByUsername(username)).getCity().getId();

        poiRequestRepository.findAll().stream().filter(poiRequestNode ->
                        poiRequestNode.getCity().getId().equals(idCityEnte))
                .filter(poiRequestNode -> Objects.isNull(poiRequestNode.getAccepted())).toList()
                .forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));

        return poiRequestDTOS;
    }

    /**
     * Gets all the Poi requests for a user as DTO
     *
     * @param username username of the user
     * @return a collection of PoiRequestDTOs
     */
    public Collection<PoiRequestDTO> getAllPoiRequestOfUserAsDTOs(String username) {
        Collection<PoiRequestDTO> poiRequestDTOS = new ArrayList<>();

        poiRequestRepository.findAll().stream()
                .filter(poiRequestNode -> poiRequestNode.getUsername().equals(username)).toList()
                .forEach(poiRequestNode -> poiRequestDTOS.add(new PoiRequestDTO(poiRequestNode)));

        return poiRequestDTOS;
    }


    /**
     * Create basic POI
     *
     * @param enteUsername username of the ente
     * @param name         name of the POI
     * @param description  description of the POI
     * @param lat          latitude of the POI
     * @param lon          longitude of the POI
     * @param timeToVisit  time needed to visit the POI
     * @param ticketPrice  price of the ticket needed to visit the POI, 0 if gratis
     * @param street       the street name of the POI
     * @param number       the number of the building of the POI
     * @return the POI created
     */
    public PointOfInterestNode createBasicPoi(String enteUsername, String name, String description, Double lat, Double lon, Integer timeToVisit, Double ticketPrice, String street, Integer number) {
        Coordinate coord = new Coordinate(lat, lon);
        coordinateRepository.save(coord);
        Address address = new Address(street, number);
        addressRepository.save(address);

        PointOfInterestNode poi = new PointOfInterestNode(name, description, coord, address, timeToVisit, ticketPrice);

        CityNode cityEnte = this.getEnteFromUser(userNodeRepository.findByUsername(enteUsername)).getCity();
        cityEnte.getPointOfInterests().add(poi);
        cityRepository.save(cityEnte);
        return poi;
    }


    /**
     * Add info to a created POI
     *
     * @param newPoi      the POI
     * @param email       the email of the POI
     * @param phone       the phone of the POI
     * @param fax         the fax of the POI
     * @param types       the types of the POI
     * @param poiTagRels  the tag-value of the POI
     * @param mapSchedule the time schedule of the week of the POI
     */
    public PointOfInterestNode addInfoToNewPoi(PointOfInterestNode newPoi, String email, String phone, String fax, Collection<String> types, Collection<Map<String, Object>> poiTagRels, Map<String, Collection<String>> mapSchedule) {
        newPoi.setTypes(this.convertStrintToPoiTypes(types));
        Contact contact = new Contact(email, phone, fax);
        contactRepository.save(contact);
        newPoi.setContact(contact);

        newPoi.setTagValues(this.convertMapToPoiTagRels(poiTagRels));

        TimeSlot timeSlot = this.createTimeSlot(mapSchedule);
        timeSlotRepository.save(timeSlot);
        newPoi.setHours(timeSlot);

        pointOfIntRepository.save(newPoi);
        return newPoi;
    }

    private Collection<PoiTagRel> convertMapToPoiTagRels(Collection<Map<String, Object>> poiTagRels) {
        Collection<PoiTagRel> values = new ArrayList<>();
        for (Map<String, Object> map : poiTagRels) {
            String tag = (String) map.get("tag");
            TagNode tagNode = tagRepository.findById(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if (!Objects.isNull(tagNode)) {
                if (tagNode.getIsBooleanType()) {
                    poiTagRel.setBooleanValue((Boolean) map.get("value"));
                } else poiTagRel.setStringValue((String) map.get("value"));
            }
            values.add(poiTagRel);
        }
        return values;
    }


    private TimeSlot createTimeSlot(Map<String, Collection<String>> mapSchedule) {
        TimeSlot timeSlot = new TimeSlot();
        for (String day : mapSchedule.keySet()) {
            if (!mapSchedule.get(day).isEmpty()) {
                switch (day) {
                    case "monday" -> mapSchedule.get(day).forEach(s -> timeSlot.getMonday().add((LocalTime.parse(s))));
                    case "tuesday" ->
                            mapSchedule.get(day).forEach(s -> timeSlot.getTuesday().add((LocalTime.parse(s))));
                    case "wednesday" ->
                            mapSchedule.get(day).forEach(s -> timeSlot.getWednesday().add((LocalTime.parse(s))));
                    case "thursday" ->
                            mapSchedule.get(day).forEach(s -> timeSlot.getThursday().add((LocalTime.parse(s))));
                    case "friday" -> mapSchedule.get(day).forEach(s -> timeSlot.getFriday().add((LocalTime.parse(s))));
                    case "saturday" ->
                            mapSchedule.get(day).forEach(s -> timeSlot.getSaturday().add((LocalTime.parse(s))));
                    case "sunday" -> mapSchedule.get(day).forEach(s -> timeSlot.getSunday().add((LocalTime.parse(s))));
                }
            }
        }
        return timeSlot;
    }


    private Collection<PoiType> convertStrintToPoiTypes(Collection<String> types) {
        Collection<PoiType> poiTypes = new ArrayList<>();
        for (String type : types) {
            if (poiTypeRepository.findById(type).isPresent()) {
                poiTypes.add(poiTypeRepository.findById(type).get());
            }
        }
        return poiTypes;
    }

    /**
     * Create POI request for creation
     *
     * @param username    username of the user that created the POI request
     * @param name        name of the POI to request
     * @param description description of the POI to request
     * @param cityDto     city of the POI to request
     * @param lat         latitude of the POI to request
     * @param lon         longitude of the POI to request
     * @param timeToVisit time needed to visit the POI to request
     * @param ticketPrice price of the ticket needed to visit the POI, 0 if free
     * @param street      the street name of the POI to request
     * @param number      the number of the building of the POI to request
     * @return the POI request created
     */
    public PoiRequestNode createPoiRequestForCreation(String username, String name, String description, CityDTO cityDto, Double lat, Double lon, Integer timeToVisit, Double ticketPrice, String street, Integer number) {
        Coordinate coord = new Coordinate(lat, lon);
        coordinateRepository.save(coord);
        Address address = new Address(street, number);
        addressRepository.save(address);
        CityNode city = cityRepository.findById(cityDto.getId()).orElseThrow();

        PoiRequestNode newPoiRequest = new PoiRequestNode(username, name, description, city, coord, address, timeToVisit, ticketPrice);
        poiRequestRepository.save(newPoiRequest);

        return newPoiRequest;
    }

    /**
     * Add info to a created POI request
     *
     * @param newPoiRequest the POI request
     * @param email         the email of the POI request
     * @param phone         the phone of the POI request
     * @param fax           the fax of the POI request
     * @param types         the types of the POI request
     * @param poiTagRels    the tag-value of the POI request
     * @param mapSchedule   the time schedule of the week of the POI request
     */
    public PoiRequestNode addInfoToNewPoiRequest(PoiRequestNode newPoiRequest, String email, String phone, String fax, Collection<String> types, Collection<Map<String, Object>> poiTagRels, Map<String, Collection<String>> mapSchedule) {
        newPoiRequest.setTypes(this.convertStrintToPoiTypes(types));
        newPoiRequest.setTagValues(this.convertMapToPoiTagRels(poiTagRels));
        Contact contact = new Contact(email, phone, fax);
        contactRepository.save(contact);
        newPoiRequest.setContact(contact);

        TimeSlot timeSlot = this.createTimeSlot(mapSchedule);
        timeSlotRepository.save(timeSlot);
        newPoiRequest.setHours(timeSlot);

        poiRequestRepository.save(newPoiRequest);
        return newPoiRequest;
    }


    /**
     * Create POI request for modification
     *
     * @param poiId       the id of the POI to modify
     * @param username    username of the user that created the POI request
     * @param name        name of the POI to request
     * @param description description of the POI to request
     * @param lat         latitude of the POI to request
     * @param lon         longitude of the POI to request
     * @param timeToVisit time needed to visit the POI to request
     * @param ticketPrice price of the ticket needed to visit the POI, 0 if free
     * @param street      the street name of the POI to request
     * @param number      the number of the building of the POI to request
     * @return the POI request created
     */
    public PoiRequestNode modifyPoiRequest(Long poiId, String username, String name, String description, Double lat, Double lon, Integer timeToVisit, Double ticketPrice, String street, Integer number) {
        PointOfInterestNode pointOfInterestNode = pointOfIntRepository.findById(poiId).orElse(null);

        CityNode city = cityRepository.findByName("Camerino"); //TODO cambiare
        /*CityNode city = cityRepository.findAll().stream().filter(c -> c.getPointOfInterests()
                .contains(pointOfInterestNode)).findFirst().orElse(null);*/

        Coordinate coord = new Coordinate(lat, lon);
        coordinateRepository.save(coord);
        Address address = new Address(street, number);
        addressRepository.save(address);

        PoiRequestNode newPoiRequest = new PoiRequestNode(username, name, description, city, coord, address, timeToVisit, ticketPrice);
        newPoiRequest.setPointOfInterestNode(pointOfInterestNode);
        poiRequestRepository.save(newPoiRequest);

        return newPoiRequest;
    }

    public PointOfInterestNode modifyPoi(PointOfInterestNode point, Map<String, Object> body) {

        point.setName((String) body.get("name"));
        point.setDescription((String) body.get("description"));
        point.getCoordinate().setLat(Double.parseDouble((String) body.get("lat")));
        point.getCoordinate().setLon(Double.parseDouble((String) body.get("lon")));
        coordinateRepository.save(point.getCoordinate());

        point.getAddress().setNumber(Integer.parseInt((String) body.get("number")));
        point.getAddress().setStreet((String) body.get("street"));
        addressRepository.save(point.getAddress());

        point.setTimeToVisit(Integer.parseInt((String) body.get("timeToVisit")));
        point.setTicketPrice(Double.parseDouble((String) body.get("price")));

        point.getContact().setEmail((String) body.get("email"));
        point.getContact().setCellNumber((String) body.get("phone"));
        point.getContact().setFax((String) body.get("fax"));
        contactRepository.save(point.getContact());

        Collection<String> daysOfWeek = new ArrayList<>(List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
        for (String day : daysOfWeek){
            try {
                Method methodSet = TimeSlot.class.getMethod("set"+day,Collection.class);
                Collection<String> a = (Collection<String>) body.get(day.toLowerCase());
                Collection<LocalTime> b = new ArrayList<>();
                a.forEach(s -> b.add((LocalTime.parse(s))));
                methodSet.invoke(point.getHours(), b);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        timeSlotRepository.save(point.getHours());

        point.setTypes(this.convertStrintToPoiTypes((Collection<String>) body.get("types")));

        point.setTagValues(this.convertMapToPoiTagRels((Collection<Map<String, Object>>) body.get("tags")));

        pointOfIntRepository.save(point);
        return point;
    }

    public PointOfInterestNode getPoiFromRequest(Long idPoiRequest) {
        if (poiRequestRepository.findById(idPoiRequest).isPresent()) {
            PoiRequestNode poiRequestNode = poiRequestRepository.findById(idPoiRequest).get();
            poiRequestNode.setAccepted(true);
            poiRequestRepository.save(poiRequestNode);
            if(poiRequestNode.getPointOfInterestNode() == null){
                return new PointOfInterestNode();
            }
            return poiRequestNode.getPointOfInterestNode();
        } else return null;
    }

    public PointOfInterestNode getPoifromId(Long poiId) {
        return pointOfIntRepository.findById(poiId).orElse(null);
    }

    public void setNewPoiInCityFromUsername(PointOfInterestNode point, String enteUsername) {
        Ente ente = this.getEnteFromUser(userNodeRepository.findByUsername(enteUsername));
        CityNode city = ente.getCity();
        city.getPointOfInterests().add(point);
        cityRepository.save(city);
    }
}
