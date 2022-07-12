package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.util.MySerializer;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
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
    private final TimeSlotRepository timeSlotRepository;
    private final MySerializer<Collection<TagNode>> tagsSerializer;

    private final EnteRepository enteRepository;

    public Ente getEnteFromUser(UserNode user){
        return enteRepository.findAll().stream().filter(ente -> ente.getUser().equals(user)).findFirst().orElseThrow();
    }


    public PointOfInterestNode createPoi(Ente ente, String name, String description, Coordinate coordinate,
                                         Address address,Contact contact,Double ticketPrice,Integer timeToVisit,
                                         PoiType ...type){
        PointOfInterestNode pointOfInterestNode = new PointOfInterestNode(name,description);
        pointOfInterestNode.setCoordinate(coordinate);
        pointOfInterestNode.getTypes().addAll(Arrays.stream(type).toList());
        pointOfInterestNode.setAddress(address);
        pointOfInterestNode.setContact(contact);
        pointOfInterestNode.setTimeToVisit(timeToVisit);
        pointOfInterestNode.setTicketPrice(ticketPrice);
        pointOfIntRepository.save(pointOfInterestNode);
        ente.getCity().getPointOfInterests().add(pointOfInterestNode);
        cityRepository.save(ente.getCity());
        return pointOfInterestNode;
    }

    public Coordinate createCoordinate(Double lat,Double...coords){
        Coordinate coordinate;
        if(coords.length==2) coordinate = new Coordinate(lat,coords[0]);
        else coordinate = new Coordinate(lat,coords[0],coords[1]);
        return coordinate;
    }

    public Collection<PoiType> getPoiTypes(){
        return poiTypeRepository.findAll();
    }

    public Collection<PoiType> getPoiTypes(Collection<CategoryNode> categoriesFilter){
        return getPoiTypes().stream().filter(t->
                t.getCategories().containsAll(categoriesFilter)).toList();
    }

    public Collection<CategoryNode> getCategories(){
        return categoryRepository.findAll();
    }

    //TODO: aggiungere timeToVisit e needTicket
    public PointOfInterestNode createPoi(Ente ente,String name,String description,
                                         Address address,Coordinate coordinate,Collection<PoiType> poiTypes,
                                         Collection<PoiTagRel> tagsAndValues,TimeSlot timeSlot,Double ticketPrice,
                                         Contact contact,Integer timeToVisit){
        PointOfInterestNode poi = new PointOfInterestNode(name,description,coordinate,address,timeSlot,timeToVisit,
                ticketPrice,contact);
        poi.getTypes().addAll(poiTypes);
        poi.getTagValues().addAll(tagsAndValues);
        pointOfIntRepository.save(poi);
        CityNode city = ente.getCity();
        city.getPointOfInterests().add(poi);
        cityRepository.save(city);
        return poi;
    }

    public Collection<TagNode> tagNodeCollectionFromJson(Object json){
        return tagsSerializer.deserialize(tagsSerializer.serialize(json),
                new TypeToken<Collection<TagNode>>(){}.getType());
    }

    public Address createAddress(String street,Integer number){
        Address result = new Address(street,number);
        addressRepository.save(result);
        return result;
    }

    public Coordinate createCoordsFromString(String lat, String lng){
        Coordinate result = new Coordinate(Double.parseDouble(lat),
                Double.parseDouble(lng));
        coordinateRepository.save(result);
        return result;
    }

    /**
     * check and update if poi is open in a certain date
     * @param poi to check
     */
    public void updateOpenPoi(PointOfInterestNode poi,Calendar calendar){
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
        //FIXME: tutto il giorno 00:00 alle 23:59
        //FIXME: refactor
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
        timeSlotRepository.save(poi.getHours());
        pointOfIntRepository.save(poi);
    }

    /**
     * check and update if all pois are open in a certain date
     * @param pois to check
     * @param date to validating the check
     */
    public void updateOpenPois(Collection<PointOfInterestNode> pois,Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        pois.forEach(pointOfInterestNode -> updateOpenPoi(pointOfInterestNode,calendar));
    }

    public void changePoiFromRequest(PoiRequestNode poiRequestNode){
        PointOfInterestNode result = poiRequestNode.getPointOfInterestNode();
        result.setHours(poiRequestNode.getTimeSlot());
        result.setCoordinate(poiRequestNode.getCoordinate());
        result.setTicketPrice(poiRequestNode.getTicketPrice());
        result.setContact(poiRequestNode.getContact());
        result.setName(poiRequestNode.getName());
        result.setDescription(poiRequestNode.getDescription());
        result.setTimeToVisit(poiRequestNode.getTimeToVisit());
        result.setAddress(poiRequestNode.getAddress());
        result.getContributors().add(poiRequestNode.getUsername());
        result.setLink(poiRequestNode.getLink());
        result.setTypes(poiRequestNode.getTypes());
        result.setTagValues(poiRequestNode.getTagValues());
        pointOfIntRepository.save(result);
    }
}
