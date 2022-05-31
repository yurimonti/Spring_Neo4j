package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class Dish {
    private String name;
    private Double price;
    private Collection<String> ingradients;

    public Dish(String name, Double price) {
        this.name = name;
        this.price = price;
        this.ingradients = new ArrayList<>();
    }
}
