package com.example.Neo4jExample.repository;

import com.example.Neo4jExample.model.Commerciante;
import com.example.Neo4jExample.model.RestaurantPoi;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RestaurantPoiRepository extends Neo4jRepository<RestaurantPoi,Long> {
    Collection<RestaurantPoi> findByOwner(Commerciante owner);
}
