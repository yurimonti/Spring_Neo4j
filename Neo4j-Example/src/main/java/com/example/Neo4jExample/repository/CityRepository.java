package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.CityNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends Neo4jRepository<CityNode,Long> {
}
