package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.Coordinate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoordinateDTO {
    private Long id;
    private Double lat;
    private Double lon;

    public CoordinateDTO(Coordinate coordinate) {
        this.id = coordinate.getId();
        this.lat = coordinate.getLat();
        this.lon = coordinate.getLon();
    }
}
