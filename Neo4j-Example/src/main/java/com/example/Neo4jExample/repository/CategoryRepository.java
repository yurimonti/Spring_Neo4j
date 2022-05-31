package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.CategoryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends Neo4jRepository<CategoryNode,String> {

}
