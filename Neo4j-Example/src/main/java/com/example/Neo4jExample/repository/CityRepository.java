package com.example.Neo4jExample.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CityRepository extends Neo4jRepository<City,Long> {
}
