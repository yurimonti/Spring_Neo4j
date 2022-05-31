package com.example.Neo4jExample.repository;


import com.example.Neo4jExample.model2.Contact;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ContactRepository extends Neo4jRepository<Contact,Long> {
}
