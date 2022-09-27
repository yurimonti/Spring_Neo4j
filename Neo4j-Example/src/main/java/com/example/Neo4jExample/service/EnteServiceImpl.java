package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnteServiceImpl implements EnteService{
    private final ItineraryService itineraryService;
    private final PoiService poiService;

    @Override
    public ItineraryNode createItinerary(Ente ente, String name, String description, Collection<String> geoJsonList,
                                         List<Long> poiIds) {
        List<PointOfInterestNode> POIs = poiIds.stream().map(this.poiService::findPoiById).toList();
        ItineraryNode result = this.itineraryService.createNewItinerary(name,description,POIs,geoJsonList,
                ente.getUser().getUsername(),true,ente.getCity().getId());
        this.itineraryService.saveItinerary(result);
        log.info("city poi number {}",ente.getCity().getPointOfInterests().size());
        return result;
    }

    @Override
    public ItineraryRequestNode createItineraryRequest(Ente ente, String name, String description,
                                                       Collection<String> geoJsonList,Collection<Long> poiIds,
                                                       Collection<CityNode> cities) {
        String createdBy = ente.getUser().getUsername();
        List<PointOfInterestNode> POIs = poiIds.stream().map(this.poiService::findPoiById).toList();
        ItineraryRequestNode result = this.itineraryService.createNewItineraryRequest(name,description,POIs,geoJsonList,
                createdBy,cities);
        result.getConsensus().add(createdBy);
        this.itineraryService.saveItineraryRequest(result);
        return result;
    }
}
