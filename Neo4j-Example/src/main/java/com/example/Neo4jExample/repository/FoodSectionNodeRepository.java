package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.FoodSectionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FoodSectionNodeRepository extends Neo4jRepository<FoodSectionNode,String> {
}
