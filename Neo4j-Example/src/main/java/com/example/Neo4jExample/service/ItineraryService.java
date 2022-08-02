package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.ItineraryNode;
import com.example.Neo4jExample.model.PoiRequestNode;
import com.example.Neo4jExample.model.PointOfInterestNode;
import com.example.Neo4jExample.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;

    /**
     * create a Itinerary and save it
     * @param pois contained in Itinerary
     * @param geojson geojson that contains information about directions
     * @param travelTime
     * @param createdBy
     * @param cities
     * @return
     */
    public ItineraryNode createItinerary(Collection<PointOfInterestNode> pois,String geojson,
                                         Integer travelTime,String createdBy,CityNode ...cities){
        ItineraryNode result = new ItineraryNode(pois,geojson,createdBy,cities);
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0, Integer::sum)+
                travelTime);
        this.itineraryRepository.save(result);
        return result;
    }

    public Collection<ItineraryNode> getItinerariesFiltered(Predicate<ItineraryNode> filter){
        return this.itineraryRepository.findAll().stream()
                .filter(filter).toList();
    }
}
