package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.ItineraryNode;
import com.example.Neo4jExample.model.PointOfInterestNode;
import com.example.Neo4jExample.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;


    public ItineraryNode createItinerary(CityNode city, Collection<PointOfInterestNode> pois,String geojson,
                                         Integer travelTime){
        ItineraryNode result = new ItineraryNode(city,pois,geojson);
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0, Integer::sum)+
                travelTime);
        this.itineraryRepository.save(result);
        return result;
    }
}
