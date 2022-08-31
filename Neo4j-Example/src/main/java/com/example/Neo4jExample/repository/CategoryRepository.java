package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.CategoryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends Neo4jRepository<CategoryNode,Long> {
    Optional<CategoryNode> findByName(String name);

}
