package com.example.Neo4jExample.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EnteRepository extends Neo4jRepository<Ente, UUID> {
    Ente findByUsername(String username);
}
