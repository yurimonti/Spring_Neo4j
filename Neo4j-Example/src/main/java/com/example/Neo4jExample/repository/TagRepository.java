package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.TagNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends Neo4jRepository<TagNode,String>{

}
