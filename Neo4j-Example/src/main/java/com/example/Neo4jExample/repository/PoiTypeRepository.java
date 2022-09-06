package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.PoiType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PoiTypeRepository extends Neo4jRepository<PoiType,Long> {
    Optional<PoiType> findByName(String name);

}
