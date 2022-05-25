package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TagRepository extends Neo4jRepository<Tag,Long>{

}
