package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Data
@NoArgsConstructor
@Node
public class DishNode {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private Double price;
    private Collection<String> ingradients;

    public DishNode(String name, Double price) {
        this.name = name;
        this.price = price;
        this.ingradients = new ArrayList<>();
    }

    public DishNode(String name,Double price,String ...ingradients){
        this(name, price);
        this.ingradients.addAll(Arrays.asList(ingradients));
    }
}