package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import com.example.Neo4jExample.repository.CityRepository;
import com.example.Neo4jExample.repository.ItineraryRepository;
import com.example.Neo4jExample.repository.ItineraryRequestRepository;
import com.example.Neo4jExample.repository.PointOfIntRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final ItineraryRequestRepository itineraryRequestRepository;
    private final UserService userService;
    private final CityRepository cityRepository;
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

    public void saveItinerary(ItineraryNode toSave){
        this.itineraryRepository.save(toSave);
    }
    public void saveItineraryRequest(ItineraryRequestNode toSave){
        this.itineraryRequestRepository.save(toSave);
    }

    /**
     * Get all the itinerary requests or a collection of filtered itinerary request if a filter is present
     * @param predicate filter to apply
     * @return all the itinerary requests or a collection of filtered itinerary request if a filter is present
     */
    public Collection<ItineraryRequestNode> getItineraryRequests(Predicate<ItineraryRequestNode> predicate) {
        Collection<ItineraryRequestNode> requests = this.itineraryRequestRepository.findAll();
        if (Objects.isNull(predicate)) return requests;
        return requests.stream().filter(predicate).toList();
    }

    /**
     * Update all the itineraries that contain the given modified point of interest
     * @param poi the modified point of interest
     */
    public void updateItinerariesByPoiModify(PointOfInterestNode poi) {
        Collection<ItineraryNode> toModify = this.itineraryRepository.findAll().stream().filter(it -> it.getPoints()
                .stream().map(i -> i.getPoi().getId()).toList()
                .contains(poi.getId())).toList();
        toModify.forEach(it -> setTimeToVisit(it, it.getPoints().stream().map(ItineraryRelPoi::getPoi).toList()));
        this.itineraryRepository.saveAll(toModify);
    }

    /**
     * Find a request by id if present
     * @param id id of the request
     * @return the request if found, null otherwise
     */
    public ItineraryRequestNode findRequestById(Long id) {
        if (this.itineraryRequestRepository.findById(id).isPresent())
            return this.itineraryRequestRepository.findById(id).get();
        else return null;
    }

    private void setRealCategory(ItineraryNode itineraryNode){
        Collection<PointOfInterestNode> pointOfInterestNodes = itineraryNode.getPoints().stream()
                .map(ItineraryRelPoi::getPoi).toList();
        Collection<CategoryNode> categoriesNodes = new ArrayList<>();
        Collection<PoiType> poiTypes = new ArrayList<>();
        pointOfInterestNodes.forEach(pointOfInterestNode -> poiTypes.addAll(pointOfInterestNode.getTypes()));
        Collection<PoiType> pois = poiTypes.stream().distinct().toList();
        pois.forEach(poiType -> categoriesNodes.addAll(poiType.getCategories()));
        itineraryNode.getCategories().addAll(categoriesNodes.stream().distinct().toList());
    }

    /**
     * Create an itinerary
     * @param pois the points of interest contained in the itinerary
     * @param geoJsonList a collection of geojsons that contains information about directions for various profiles
     * @param createdBy username of the user who creates the itinerary
     * @param cities a collection of cities that the itinerary passes through
     * @return the created itinerary
     */
    //FIXME: cancella tutti i poi nelle città che non sono compresi nell'itinerario...
    public ItineraryNode createItinerary(String name, String description, Collection<PointOfInterestNode> pois,
                                         Collection<String> geoJsonList, String createdBy, Boolean isDefault,
                                         CityNode... cities) {
        for (CityNode c : cities) {
            log.info("pre new -> save city: {} {} -> number of poi {}",c.getId(),c.getName(),c.getPointOfInterests()
                    .size());
        }
        ItineraryNode result = new ItineraryNode();
        for (CityNode c : result.getCities()) {
            log.info("post new -> save city: {} {} -> number of poi {}",c.getId(),c.getName(),c.getPointOfInterests()
                    .size());
        }
        result.setName(name);
        result.setDescription(description);
        result.getPoints().addAll(this.indexedPoints(pois));
        result.getGeoJsonList().addAll(geoJsonList);
        result.setCreatedBy(createdBy);
        result.setIsDefault(isDefault);
        /*for (CityNode city : cities) {
            result.getCities().add(city);
        }*/
        this.setRealCategory(result);
        this.setTimeToVisit(result, pois);
        for (CityNode c : cities) {
            log.info("set time->city: {} {} -> number of poi {}",c.getId(),c.getName(),c.getPointOfInterests().size());
        }
        /*ItineraryNode result = new ItineraryNode(name, description, this.indexedPoints(pois), geoJsonList, createdBy,
                isDefault, cities);*/

        for (CityNode c : cities) {
            log.info("pre save city: {} {} -> number of poi {}",c.getId(),c.getName(),c.getPointOfInterests().size());
        }
        //FIXME: problema causato da questa linea di codice
        this.itineraryRepository.save(result);
        for (CityNode c : cities) {
            log.info("1 save city: {} {} -> number of poi {}",c.getId(),c.getName(),c.getPointOfInterests().size());
        }
        log.info("itinerary : {} created",result.getName());
        return result;
    }

    /**
     * Create an itinerary request
     * @param pois the points of interest contained in the itinerary request
     * @param geoJsonList a collection of geojsons that contains information about directions for various profiles
     * @param createdBy username of the user who creates the itinerary request
     * @param cities a collection of cities that the itinerary passes through
     * @return the created itinerary request
     */
    public ItineraryRequestNode createItineraryRequest(String name, String description,
                                                       Collection<PointOfInterestNode> pois,
                                                       Collection<String> geoJsonList, String createdBy,
                                                       CityNode... cities) {
        ItineraryRequestNode result = new ItineraryRequestNode(name, description, this.indexedPoints(pois), geoJsonList,
                createdBy, cities);
        setTimeToVisit(result, pois);
        log.info("added timetovisit to itinerary request : {}",result.getName());
        if (this.userService.userHasRole(createdBy, "ente"))
            result.getConsensus().add(createdBy);
        this.itineraryRequestRepository.save(result);
        log.info("itinerary saved : {}",result.getName());
        return result;
    }

    private Collection<ItineraryRelPoi> createItineraryRelFromRequest(ItineraryRequestNode from) {
        Collection<ItineraryRelPoi> result = new ArrayList<>();
        from.getPoints().forEach(rel -> result.add(new ItineraryRelPoi(rel.getPoi(), rel.getIndex())));
        return result;
    }

    /**
     * Create an itinerary request from a request
     * @param from the itinerary request that contains the values to copy
     * @return the created itinerary
     */
    public ItineraryNode createItineraryFromRequest(ItineraryRequestNode from) {
        ItineraryNode result = new ItineraryNode(from.getName(), from.getDescription(),
                this.createItineraryRelFromRequest(from), from.getGeoJsonList(), from.getCreatedBy(), true,
                from.getCities().toArray(CityNode[]::new));
        result.setTimeToVisit(from.getTimeToVisit());
        this.itineraryRepository.save(result);
        return result;
    }

    /**
     * Update the consensus of an itinerary request
     * @param ente the ente updating the consensus
     * @param target the itinerary request
     * @param consensus the value of the consensus
     * @return the itinerary if all the consensus in the request are now true, null otherwise
     */
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
            log.info("points in itinerary accepted : {}",result.getPoints().stream().map(ItineraryRelPoi::getPoi).toList());
        }
        this.itineraryRequestRepository.save(target);
        log.info("itinerary saved : {}",target.getName());
        return result;
    }

    /**
     * Delete an itinerary
     * @param toDelete the itinerary to delete
     */
    public void deleteItinerary(ItineraryNode toDelete) {
        this.itineraryRepository.delete(toDelete);
    }

    /**
     * Get a collection of filtered itineraries
     * @param filter the filter
     * @return a collection of filtered itineraries
     */
    public Collection<ItineraryNode> getItinerariesFiltered(Predicate<ItineraryNode> filter) {
        return this.itineraryRepository.findAll().stream()
                .filter(filter).toList();
    }

    /**
     * Find an itinerary given its id
     * @param id the id of the itinerary
     * @return the itinerary with the specified id or null if not found
     */
    public ItineraryNode findItineraryById(Long id) {
        if (this.itineraryRepository.findById(id).isPresent())
            return this.itineraryRepository.findById(id).get();
        else return null;
    }

    // nuovi metodi per creazione itinerario
    public ItineraryNode createNewItinerary(String name, String description, Collection<PointOfInterestNode> POIs,
                                          Collection<String> geoJsonList, String createdBy, Boolean isDefault,
                                          Long... cities){
        ItineraryNode result = new ItineraryNode(name,description,createdBy,isDefault);
        for (Long city : cities) {
            result.getCities().add(this.cityRepository.findById(city).get());
        }
        result.setGeoJsonList(geoJsonList);
        result.getPoints().addAll(this.indexedPoints(POIs));
        this.setRealCategory(result);
        this.setTimeToVisit(result,POIs);
        return result;
    }
    public ItineraryRequestNode createNewItineraryRequest(String name, String description,
                                                          Collection<PointOfInterestNode> POIs,
                                                          Collection<String> geoJsonList, String createdBy,
                                                          Collection<CityNode> cities){
        ItineraryRequestNode result = new ItineraryRequestNode(name, description,createdBy);
        result.setCities(cities);
        result.setGeoJsonList(geoJsonList);
        result.getPoints().addAll(this.indexedPoints(POIs));
        this.setTimeToVisit(result, POIs);
        return result;
    }
}
