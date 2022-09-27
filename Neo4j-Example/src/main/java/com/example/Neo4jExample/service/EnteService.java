package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.Ente;
import com.example.Neo4jExample.model.ItineraryNode;
import com.example.Neo4jExample.model.ItineraryRequestNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface EnteService {
    ItineraryNode createItinerary(Ente ente, String name, String description,Collection<String> geoJsonList,
                                  List<Long> poiIds);
    ItineraryRequestNode createItineraryRequest(Ente ente, String name, String description,
                                                Collection<String> geoJsonList,Collection<Long> poiIds,
                                                Collection<CityNode> cities);
}
