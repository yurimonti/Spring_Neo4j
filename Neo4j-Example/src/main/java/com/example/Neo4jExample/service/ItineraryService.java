package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.ItineraryRepository;
import com.example.Neo4jExample.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
//FIXME: rivedere i tempi di visita
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;

    private void setTimeToVisit(ItineraryNode result, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum)*60);
    }

    private void setTimeToVisit(ItineraryRequestNode result, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum)*60);
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
     *
     * @param predicate filter to apply
     * @return all itineraryRequests if is not presente a predicate, otherwise returns filtered by predicate
     */
    public Collection<ItineraryRequestNode> getItineraryRequests(Predicate<ItineraryRequestNode> predicate) {
        Collection<ItineraryRequestNode> requests = this.itineraryRequestRepository.findAll();
        if(Objects.isNull(predicate)) return requests;
        return requests.stream().filter(predicate).toList();
    }
    /**
     * uodate all itineraries that contain the given poi modified
     * @param poi modified
     */
    public void updateItinerariesByPoiModify(PointOfInterestNode poi){
        Collection<ItineraryNode> toModify = this.itineraryRepository.findAll().stream().filter(it -> it.getPoints()
                .stream().map(ItineraryRelPoi::getPoi).toList()
                .contains(poi)).toList();
        toModify.forEach(it -> setTimeToVisit(it,it.getPoints().stream().map(ItineraryRelPoi::getPoi).toList()));
        this.itineraryRepository.saveAll(toModify);
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
     * @param geoJsonList collection of geojson that contains information about directions for various Profiles
     * @param createdBy  user who creates itinerary
     * @param cities     that contain this itinerary
     * @return a created Itinerary
     */
    public ItineraryNode createItinerary(String name,String description,Collection<PointOfInterestNode> pois,
                                         Collection<String> geoJsonList,String createdBy,Boolean isDefault,
                                         CityNode... cities) {
        ItineraryNode result = new ItineraryNode(name,description,indexedPoints(pois), geoJsonList, createdBy,isDefault, cities);
        setTimeToVisit(result, pois);
        this.itineraryRepository.save(result);
        return result;
    }

    /**
     * create an ItineraryRequest and save it
     *
     * @param pois       contained in Itinerary
     * @param geoJsonList collection of geojson that contains information about directions for various Profiles
     * @param createdBy  user who creates itinerary
     * @param cities     that contain this itinerary
     * @return a created ItineraryRequest
     */
    public ItineraryRequestNode createItineraryRequest(String name,String description,
                                                       Collection<PointOfInterestNode> pois,
                                                       Collection<String> geoJsonList,String createdBy,
                                                       CityNode... cities) {
        ItineraryRequestNode result = new ItineraryRequestNode(name,description,this.indexedPoints(pois), geoJsonList,
                createdBy, cities);
        setTimeToVisit(result, pois);
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
                from.getGeoJsonList(), from.getCreatedBy(),true,from.getCities().toArray(CityNode[]::new));
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
