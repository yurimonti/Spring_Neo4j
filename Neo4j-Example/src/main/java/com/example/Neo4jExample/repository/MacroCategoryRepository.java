package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.MacroCategory;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MacroCategoryRepository extends Neo4jRepository<MacroCategory,Long> {

}
