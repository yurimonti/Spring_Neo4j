package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.CityRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProvaService {
    private final PointOfIntRepository pointOfIntRepository;
    private final CityRepository cityRepository;

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
}
