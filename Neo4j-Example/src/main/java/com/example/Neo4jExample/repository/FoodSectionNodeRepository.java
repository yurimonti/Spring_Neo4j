package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.FoodSectionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodSectionNodeRepository extends Neo4jRepository<FoodSectionNode,Long> {
}
