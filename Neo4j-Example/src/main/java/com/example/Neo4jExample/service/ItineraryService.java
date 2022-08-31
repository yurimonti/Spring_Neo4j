package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.ItineraryRepository;
import com.example.Neo4jExample.repository.ItineraryRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;
    private final UserService userService;

    private final PoiService poiService;

    private void setTimeToVisit(ItineraryNode result, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum) * 60);
    }

    private void setTimeToVisit(ItineraryRequestNode result, Collection<PointOfInterestNode> pois) {
        result.setTimeToVisit(pois.stream().map(PointOfInterestNode::getTimeToVisit).reduce(0.0, Double::sum) * 60);
    }

    private Collection<ItineraryRelPoi> indexedPoints(Collection<PointOfInterestNode> points) {
        Collection<ItineraryRelPoi> pointsNodes = new ArrayList<>();
        PointOfInterestNode[] nodes = points.toArray(PointOfInterestNode[]::new);
        for (int i = 0; i < points.size(); i++) {
            PointOfInterestNode node = nodes[i];
            pointsNodes.add(new ItineraryRelPoi(node, i));
        }
        return pointsNodes;
    }

    /**
     * @param predicate filter to apply
     * @return all itineraryRequests if is not presente a predicate, otherwise returns filtered by predicate
     */
    public Collection<ItineraryRequestNode> getItineraryRequests(Predicate<ItineraryRequestNode> predicate) {
        Collection<ItineraryRequestNode> requests = this.itineraryRequestRepository.findAll();
        if (Objects.isNull(predicate)) return requests;
        return requests.stream().filter(predicate).toList();
    }

    /**
     * uodate all itineraries that contain the given poi modified
     *
     * @param poi modified
     */
    public void updateItinerariesByPoiModify(PointOfInterestNode poi) {
        Collection<ItineraryNode> toModify = this.itineraryRepository.findAll().stream().filter(it -> it.getPoints()
                .stream().map(ItineraryRelPoi::getPoi).toList()
                .contains(poi)).toList();
        toModify.forEach(it -> setTimeToVisit(it, it.getPoints().stream().map(ItineraryRelPoi::getPoi).toList()));
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
     * @param pois        contained in Itinerary
     * @param geoJsonList collection of geojson that contains information about directions for various Profiles
     * @param createdBy   user who creates itinerary
     * @param cities      that contain this itinerary
     * @return a created Itinerary
     */
    public ItineraryNode createItinerary(String name, String description, Collection<PointOfInterestNode> pois,
                                         Collection<String> geoJsonList, String createdBy, Boolean isDefault,
                                         CityNode... cities) {
        ItineraryNode result = new ItineraryNode(name, description, indexedPoints(pois), geoJsonList, createdBy, isDefault, cities);
        log.info("itinerary without save it : {}",result);
        setTimeToVisit(result, pois);
        log.info("added timetovisit to itinerary : {}",result);
        this.itineraryRepository.save(result);
        log.info("itinerary after save : {}",result);
        return result;
    }

    /**
     * create an ItineraryRequest and save it
     *
     * @param pois        contained in Itinerary
     * @param geoJsonList collection of geojson that contains information about directions for various Profiles
     * @param createdBy   user who creates itinerary
     * @param cities      that contain this itinerary
     * @return a created ItineraryRequest
     */
    public ItineraryRequestNode createItineraryRequest(String name, String description,
                                                       Collection<PointOfInterestNode> pois,
                                                       Collection<String> geoJsonList, String createdBy,
                                                       CityNode... cities) {
        ItineraryRequestNode result = new ItineraryRequestNode(name, description, this.indexedPoints(pois), geoJsonList,
                createdBy, cities);
        log.info("itinerary request without save it : {}",result);
        setTimeToVisit(result, pois);
        log.info("added timetovisit to itinerary request : {}",result);
        if (this.userService.userHasRole(createdBy, "ente"))
            result.getConsensus().add(createdBy);
        this.itineraryRequestRepository.save(result);
        log.info("itinerary request after save : {}",result);
        return result;
    }

    private Collection<ItineraryRelPoi> createItineraryRelFromRequest(ItineraryRequestNode from) {
        Collection<ItineraryRelPoi> result = new ArrayList<>();
        from.getPoints().forEach(rel -> result.add(new ItineraryRelPoi(rel.getPoi(), rel.getIndex())));
        return result;
    }

    /**
     * create an ItineraryRequestNode from Request and save it
     *
     * @param from itinerary request that contains values
     * @return created Itinerary
     */
    public ItineraryNode createItineraryFromRequest(ItineraryRequestNode from) {
        ItineraryNode result = new ItineraryNode(from.getName(), from.getDescription(),
                this.createItineraryRelFromRequest(from), from.getGeoJsonList(), from.getCreatedBy(), true,
                from.getCities().toArray(CityNode[]::new));
        result.setTimeToVisit(from.getTimeToVisit());
        this.itineraryRepository.save(result);
        return result;
    }

    //FIXME: vedere perche non rimangono i POI
    public ItineraryNode updateConsensus(Ente ente, ItineraryRequestNode target, boolean consensus) {
        ItineraryNode result = null;
        if (!consensus) {
            target.setAccepted(false);
            log.info("consensus to : {}",consensus);
        } else {
            if (!target.getConsensus().contains(ente.getUser().getUsername()) && target.getCities().contains(ente.getCity()))
                target.getConsensus().add(ente.getUser().getUsername());
            log.info("consensus to : {}",consensus);
        }
        if (target.getConsensus().size() == target.getCities().size()) {
            target.setAccepted(true);
            log.info("accepted!");
            result = this.createItineraryFromRequest(target);
            log.info("points in itinerary accepted : {}",result.getPoints());
        }
        this.itineraryRequestRepository.save(target);
        log.info("itinerary after save it : {}",target);
        return result;
    }

    /**
     * Delete an Itinerary from App
     *
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
