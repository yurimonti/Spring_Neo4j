package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.PoiRequestNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiRequestRepository extends Neo4jRepository<PoiRequestNode,Long> {
}
