package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.ItineraryRepository;
import com.example.Neo4jExample.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
/**
 * A Service that permits to manage itineraries.
 */
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;

    private void setTimeToVisit(ItineraryNode result, Integer travelTime, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0, Integer::sum) +
                travelTime);
    }

    private void setTimeToVisit(ItineraryRequestNode result, Integer travelTime, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0, Integer::sum) +
                travelTime);
    }

    /**
     * find and return a request by id if is present
     *
     * @param id of request
     * @return request by id
     */
    public ItineraryRequestNode findRequestById(Long id) {
        if (this.itineraryRequestRepository.findById(id).isPresent())
            return this.itineraryRequestRepository.findById(id).get();
        else return null;
    }

    /**
     * create an Itinerary and save it
     *
     * @param pois       contained in Itinerary
     * @param geojson    geojson that contains information about directions
     * @param travelTime elapsing time between pois
     * @param createdBy  user who creates itinerary
     * @param cities     that contain this itinerary
     * @return a created Itinerary
     */
    public ItineraryNode createItinerary(Collection<PointOfInterestNode> pois, String geojson,
                                         Integer travelTime, String createdBy, CityNode... cities) {
        ItineraryNode result = new ItineraryNode(pois, geojson, createdBy, cities);
        setTimeToVisit(result, travelTime, pois);
        this.itineraryRepository.save(result);
        return result;
    }

    /**
     * create an ItineraryRequest and save it
     *
     * @param pois       contained in Itinerary
     * @param geojson    geojson that contains information about directions
     * @param travelTime elapsing time between pois
     * @param createdBy  user who creates itinerary
     * @param cities     that contain this itinerary
     * @return a created ItineraryRequest
     */
    public ItineraryRequestNode createItineraryRequest(Collection<PointOfInterestNode> pois, String geojson,
                                                       Integer travelTime, String createdBy, CityNode... cities) {
        ItineraryRequestNode result = new ItineraryRequestNode(pois, geojson, createdBy, cities);
        setTimeToVisit(result, travelTime, pois);
        result.getConsensus().add(createdBy);
        this.itineraryRequestRepository.save(result);
        return result;
    }

    /**
     * create an ItineraryRequestNode from Request and save it
     *
     * @param from itinerary request that contains values
     * @return created Itinerary
     */
    public ItineraryNode createItineraryFromRequest(ItineraryRequestNode from) {
        ItineraryNode result = new ItineraryNode(from.getPoints(), from.getGeojson(), from.getCreatedBy(),
                from.getCities().toArray(CityNode[]::new));
        result.setTimeToVisit(from.getTimeToVisit());
        this.itineraryRepository.save(result);
        return result;
    }

    public ItineraryNode updateConsensus(Ente ente, ItineraryRequestNode target, boolean consensus) {
        ItineraryNode result = null;
        if (!consensus) {
            target.setAccepted(false);
        }
        else {
            if(!target.getConsensus().contains(ente.getUser().getUsername()))
                target.getConsensus().add(ente.getUser().getUsername());
        }
        if(target.getConsensus().size() == target.getCities().size()) {
            target.setAccepted(true);
            result = this.createItineraryFromRequest(target);
        }
        this.itineraryRequestRepository.save(target);
        return result;
    }

    /**
     * filter itineraries with a certain filter
     *
     * @param filter applicated to
     * @return Itineraries filtered
     */
    public Collection<ItineraryNode> getItinerariesFiltered(Predicate<ItineraryNode> filter) {
        return this.itineraryRepository.findAll().stream()
                .filter(filter).toList();
    }
}
