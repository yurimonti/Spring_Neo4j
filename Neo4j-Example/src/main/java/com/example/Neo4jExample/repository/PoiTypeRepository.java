package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.PoiType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiTypeRepository extends Neo4jRepository<PoiType,String> {

}
