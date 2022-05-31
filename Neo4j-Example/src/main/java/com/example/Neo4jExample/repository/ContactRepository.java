package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.Contact;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends Neo4jRepository<Contact,Long> {
}
