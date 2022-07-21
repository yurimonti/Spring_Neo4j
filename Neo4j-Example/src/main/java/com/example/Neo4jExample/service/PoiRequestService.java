package com.example.Neo4jExample.service;

import com.example.Neo4jExample.repository.PoiRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoiRequestService {
    private final PoiRequestRepository poiRequestRepository;
}
