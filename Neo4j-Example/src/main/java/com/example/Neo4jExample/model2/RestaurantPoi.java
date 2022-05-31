package com.example.Neo4jExample.model2;

import com.example.Neo4jExample.model.Menu;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
public class RestaurantPoi {

    @Id @GeneratedValue
    private Long id;
    private Menu menu;
}
