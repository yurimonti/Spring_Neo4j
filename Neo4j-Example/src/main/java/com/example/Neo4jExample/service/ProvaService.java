package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.*;
import com.example.Neo4jExample.service.util.MySerializer;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProvaService {
    private final PointOfIntRepository pointOfIntRepository;
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final PoiTypeRepository poiTypeRepository;
    private final CoordinateRepository coordinateRepository;
    private final AddressRepository addressRepository;
    private final MySerializer<Collection<TagNode>> tagsSerializer;

    public PointOfInterestNode createPoi(Ente ente, String name, String description, Coordinate coordinate,
                                         Address address,Contact contact,Boolean needTicket,Integer timeToVisit,
                                         PoiType ...type){
        PointOfInterestNode pointOfInterestNode = new PointOfInterestNode(name,description);
        pointOfInterestNode.setCoordinate(coordinate);
        pointOfInterestNode.getTypes().addAll(Arrays.stream(type).toList());
        pointOfInterestNode.setAddress(address);
        pointOfInterestNode.setContact(contact);
        pointOfInterestNode.setTimeToVisit(timeToVisit);
        pointOfInterestNode.setNeedTicket(needTicket);
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

    public PointOfInterestNode createPoi(Ente ente,String name,String description,
                                         Address address,Coordinate coordinate,Collection<PoiType> poiTypes,
                                         Collection<PoiTagRel> tagsAndValues){
        PointOfInterestNode poi = new PointOfInterestNode(name,description,coordinate,address);
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
}
