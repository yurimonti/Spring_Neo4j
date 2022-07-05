package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.CityNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CityDTO {
    private Long id;
    private String name;

    public CityDTO(CityNode cityNode) {
        this.id = cityNode.getId();
        this.name = cityNode.getName();
    }
}
