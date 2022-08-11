package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.ItineraryRepository;
import com.example.Neo4jExample.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
//FIXME: rivedere i tempi di visita
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;

    private void setTimeToVisit(ItineraryNode result, Double travelTime, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum)*60 +
                travelTime);
    }

    private void setTimeToVisit(ItineraryRequestNode result, Double travelTime, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum)*60 +
                travelTime);
    }

    private Collection<ItineraryRelPoi> indexedPoints(Collection<PointOfInterestNode> points) {
        Collection<ItineraryRelPoi> pointsNodes = new ArrayList<>();
        PointOfInterestNode[] nodes = points.toArray(PointOfInterestNode[]::new);
        for (int i = 0; i < points.size(); i++) {
            PointOfInterestNode node = nodes[i];
            pointsNodes.add(new ItineraryRelPoi(node,i));
        }
        return pointsNodes;
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
    public ItineraryNode createItinerary(String name,String description,Collection<PointOfInterestNode> pois, String geojson,
                                         Double travelTime, String createdBy,Boolean isDefault,CityNode... cities) {
        ItineraryNode result = new ItineraryNode(name,description,indexedPoints(pois), geojson, createdBy,isDefault, cities);
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
    public ItineraryRequestNode createItineraryRequest(String name,String description,Collection<PointOfInterestNode> pois, String geojson,
                                                       Double travelTime, String createdBy, CityNode... cities) {
        ItineraryRequestNode result = new ItineraryRequestNode(name,description,this.indexedPoints(pois), geojson, createdBy, cities);
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
        ItineraryNode result = new ItineraryNode(from.getName(), from.getDescription(),from.getPoints(),
                from.getGeojson(), from.getCreatedBy(),true,from.getCities().toArray(CityNode[]::new));
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
     * Delete an Itinerary from App
     * @param toDelete Itinerary to delete
     */
    public void deleteItinerary(ItineraryNode toDelete) {
        this.itineraryRepository.delete(toDelete);
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

    public ItineraryNode findItineraryById(Long id) {
        if (this.itineraryRepository.findById(id).isPresent())
            return this.itineraryRepository.findById(id).get();
        else return null;
    }
}
