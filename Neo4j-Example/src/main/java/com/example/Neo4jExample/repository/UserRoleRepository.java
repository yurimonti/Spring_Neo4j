package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.UserRole;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends Neo4jRepository<UserRole,Long> {
    UserRole findByName(String name);
}
