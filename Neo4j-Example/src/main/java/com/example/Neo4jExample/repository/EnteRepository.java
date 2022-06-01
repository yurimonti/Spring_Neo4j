package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.Ente;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EnteRepository extends Neo4jRepository<Ente, UUID> {
}
