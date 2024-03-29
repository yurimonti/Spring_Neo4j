package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNodeRepository extends Neo4jRepository<UserNode,String> {
    UserNode findByUsername(String username);
}
