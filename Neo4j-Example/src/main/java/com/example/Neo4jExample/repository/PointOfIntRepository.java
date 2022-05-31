package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.PointOfInterestNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PointOfIntRepository extends Neo4jRepository<PointOfInterestNode,Long> {
}
