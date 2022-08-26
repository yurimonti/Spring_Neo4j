package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.CityNode;
import com.example.Neo4jExample.model.PoiRequestNode;
import com.example.Neo4jExample.model.PointOfInterestNode;

import java.util.Map;

public interface PoiServiceInterface {
    void deletePoi(Long poiId) throws NullPointerException;
    void savePoi(PointOfInterestNode poiToSave);
    PointOfInterestNode getPoiById(Long poiId) throws Exception;
    boolean poiIsContainedInCity(PointOfInterestNode isContained, CityNode from);
    void modifyPoiFromBody(PointOfInterestNode toModify, Map<String, Object> bodyFrom);
    void modifyPoiFromRequestNode(PointOfInterestNode toModify, PoiRequestNode from);
}
