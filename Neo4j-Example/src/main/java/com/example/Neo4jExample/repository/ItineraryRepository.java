package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.ItineraryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ItineraryRepository extends Neo4jRepository<ItineraryNode,Long> {
    Collection<ItineraryNode> getAllByCity(CityNode city);
}
