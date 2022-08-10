package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.ItineraryRelPoi;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ItineraryRelPoiDTO {
    private PoiDTO poi;
    private Integer index;

    public ItineraryRelPoiDTO(ItineraryRelPoi itineraryRelPoi) {
        this.poi = new PoiDTO(itineraryRelPoi.getPoi());
        this.index = itineraryRelPoi.getIndex();
    }
}
