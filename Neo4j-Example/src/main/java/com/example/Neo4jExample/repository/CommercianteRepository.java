package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.Commerciante;
import com.example.Neo4jExample.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CommercianteRepository extends Neo4jRepository<Commerciante,Long> {
    Commerciante findByUser(UserNode user);
}
