package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.CityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CityRepository extends Neo4jRepository<CityNode,Long> {
}
