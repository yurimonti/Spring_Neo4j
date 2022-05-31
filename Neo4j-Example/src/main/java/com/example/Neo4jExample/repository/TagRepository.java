package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.TagNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TagRepository extends Neo4jRepository<TagNode,Long>{

}
