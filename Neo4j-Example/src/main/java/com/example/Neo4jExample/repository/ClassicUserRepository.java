package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.ClassicUserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassicUserRepository extends Neo4jRepository<ClassicUserNode,Long> {
}
