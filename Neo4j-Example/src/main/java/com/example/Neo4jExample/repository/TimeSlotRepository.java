package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model2.TimeSlot;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TimeSlotRepository extends Neo4jRepository<TimeSlot,Long> {
}
