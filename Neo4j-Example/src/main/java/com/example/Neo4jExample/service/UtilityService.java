package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UtilityService {
    private final TagRepository tagRepository;
    private final AddressRepository addressRepository;
    private final CoordinateRepository coordinateRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ContactRepository contactRepository;
    private final CityRepository cityRepository;
    private final PointOfIntRepository pointOfIntRepository;
    private final CategoryRepository categoryRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final ItineraryService itineraryService;

    /**
     * Create coordinates from string values
     * @param lat latitude in string value
     * @param lng longitude in string value
     * @return the new coordinates
     */
    public Coordinate createCoordsFromString(String lat, String lng){
        Coordinate result = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lng));
        this.coordinateRepository.save(result);
        return result;
    }

    /**
     * Create an address from values
     * @param street name of street
     * @param number number of the building
     * @return the new address
     */
    public Address createAddress(String street, Integer number){
        Address result = new Address(street,number);
        this.addressRepository.save(result);
        return result;
    }

    /**
     * Create a contact from values
     * @param email email of the contact
     * @param cellNumber phone number of the contact
     * @param fax fax of the contact
     * @return the new contact
     */
    public Contact createContact(String email,String cellNumber,String fax){
        Contact result = new Contact(email,cellNumber,fax);
        this.contactRepository.save(result);
        return result;
    }

    /**
     * Fill a TimeSlot with params contained in a body request
     * @param toFill   TimeSlot to fill
     * @param bodyFrom body of the request
     * @return the filled TimeSlot
     */
    public TimeSlot getTimeSlotFromBody(TimeSlot toFill, Map<String, Object> bodyFrom) {
        Collection<String> monday = (Collection<String>) bodyFrom.get("monday");
        Collection<String> tuesday = (Collection<String>) bodyFrom.get("tuesday");
        Collection<String> wednesday = (Collection<String>) bodyFrom.get("wednesday");
        Collection<String> thursday = (Collection<String>) bodyFrom.get("thursday");
        Collection<String> friday = (Collection<String>) bodyFrom.get("friday");
        Collection<String> saturday = (Collection<String>) bodyFrom.get("saturday");
        Collection<String> sunday = (Collection<String>) bodyFrom.get("sunday");
        if (!monday.isEmpty()) monday.forEach(s -> toFill.getMonday().add((LocalTime.parse(s))));
        if (!tuesday.isEmpty()) tuesday.forEach(s -> toFill.getTuesday().add((LocalTime.parse(s))));
        if (!wednesday.isEmpty()) wednesday.forEach(s -> toFill.getWednesday().add((LocalTime.parse(s))));
        if (!thursday.isEmpty()) thursday.forEach(s -> toFill.getThursday().add((LocalTime.parse(s))));
        if (!friday.isEmpty()) friday.forEach(s -> toFill.getFriday().add((LocalTime.parse(s))));
        if (!saturday.isEmpty()) saturday.forEach(s -> toFill.getSaturday().add((LocalTime.parse(s))));
        if (!sunday.isEmpty()) sunday.forEach(s -> toFill.getSunday().add((LocalTime.parse(s))));
        this.timeSlotRepository.save(toFill);
        return toFill;
    }

    /**
     * Get a value from a http body request
     * @param key key of value
     * @param bodyFrom http body request
     * @return the value from key
     */
    public String getValueFromBody(String key, Map<String, Object> bodyFrom) {
        return (String) bodyFrom.get(key);
    }

    /**
     * Create a collection of PoiTagRels from a body request
     * @param from body request
     * @return a collection of PoiTagRels from a body request
     */
    public Collection<PoiTagRel> createPoiTagRel(Collection<Map<String, Object>> from) {
        Collection<PoiTagRel> result = new ArrayList<>();
        for (Map<String, Object> map : from) {
            String tag = (String) map.get("tag");
            TagNode tagNode = this.tagRepository.findByName(tag).orElse(null);
            PoiTagRel poiTagRel = new PoiTagRel(tagNode);
            if (!Objects.isNull(tagNode)) {
                if (tagNode.getIsBooleanType()) {
                    poiTagRel.setBooleanValue((Boolean) map.get("value"));
                } else poiTagRel.setStringValue((String) map.get("value"));
            }
            result.add(poiTagRel);
        }
        return result;
    }

    /**
     * Get the City of a certain PointOfInterestNode
     * @param poiId the id of the PointOfInterestNode
     * @return the City of the PointOfInterestNode
     */
    public CityNode getCityOfPoi(Long poiId) {
        PointOfInterestNode poi = this.pointOfIntRepository.findById(poiId)
                .orElseThrow(()->new NullPointerException("Poi not found"));
        return this.cityRepository.findAll().stream().filter(c -> c.getPointOfInterests().contains(poi))
                .findFirst().orElseThrow(()-> new NullPointerException("No city found for this poi"));
        /*return this.cityRepository.findAll().stream().filter(cityNode -> cityNode.getPointOfInterests().stream()
                        .map(PointOfInterestNode::getId).toList().contains(poiId))
                .findFirst().orElse(null);*/
    }

    /**
     * Get all the points of interest in the db
     * @return a collection of the points of interest in the db
     */
    public Collection<PointOfInterestNode> getAllPois() {
        return pointOfIntRepository.findAll();
    }

    /**
     * Get all the cities in the db
     * @return a collection of the cities in the db
     */
    public Collection<CityNode> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Get all the poi types in the db
     * @return a collection of the poi types in the db
     */
    public Collection<PoiType> getPoiTypes(){
        return poiTypeRepository.findAll();
    }

    /**
     * Get all the poi types of a collection of categories in the db
     * @param categoriesFilter the collection of categories
     * @return all the poi types of a collection of categories in the db
     */
    public Collection<PoiType> getPoiTypes(Collection<CategoryNode> categoriesFilter){
        return getPoiTypes().stream().filter(t->
                t.getCategories().containsAll(categoriesFilter)).toList();
    }

    /**
     * Get all the categories in the db
     * @return a collection of the categories in the db
     */
    public Collection<CategoryNode> getCategories(){
        return categoryRepository.findAll();
    }



    /**
     * Check and update if poi is open in a certain date
     * @param poi to check
     */
    public void updateOpenPoi(PointOfInterestNode poi, Calendar calendar){
        Collection<LocalTime> day = switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> poi.getHours().getMonday();
            case Calendar.TUESDAY -> poi.getHours().getTuesday();
            case Calendar.WEDNESDAY -> poi.getHours().getWednesday();
            case Calendar.THURSDAY -> poi.getHours().getThursday();
            case Calendar.FRIDAY -> poi.getHours().getFriday();
            case Calendar.SATURDAY -> poi.getHours().getSaturday();
            case Calendar.SUNDAY -> poi.getHours().getSunday();
            default -> new ArrayList<>();
        };
        boolean toSet = false;
        Instant instant = calendar.toInstant();
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        LocalTime toCompare = LocalTime.ofInstant(instant, zoneId);
        int l = day.size();
        if(l==1) toSet = true;
        else if (l>2){
            if((day.stream().toList().get(0).isBefore(toCompare) && day.stream().toList().get(1).isAfter(toCompare))||
                    (day.stream().toList().get(2).isBefore(toCompare) &&
                            day.stream().toList().get(3).isAfter(toCompare))) toSet = true;
        }else if(l==2){
            if(day.stream().toList().get(0).isBefore(toCompare) && day.stream().toList().get(1).isAfter(toCompare))
                toSet = true;
        }
        poi.getHours().setIsOpen(toSet);
        this.timeSlotRepository.save(poi.getHours());
        this.pointOfIntRepository.save(poi);
    }

    /**
     * Check and update if all pois are open in a certain date
     * @param date to validating the check
     */
    public void updateOpenPois(Date date){
        Collection<PointOfInterestNode> pois = this.pointOfIntRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        pois.forEach(pointOfInterestNode -> this.updateOpenPoi(pointOfInterestNode,calendar));
    }
}
