package com.example.Neo4jExample.model;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface StartRelRepo extends Neo4jRepository<StartRel,Long> {
    StartRel findByName(String name);
}
