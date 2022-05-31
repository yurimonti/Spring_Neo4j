package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.Coordinate;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CoordinateRepository extends Neo4jRepository<Coordinate,Long> {
}
