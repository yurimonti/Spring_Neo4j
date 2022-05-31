package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class FoodSection {
    private String name;
    private Collection<Dish> dishes;

    public FoodSection(String name) {
        this.name = name;
        this.dishes = new ArrayList<>();
    }
}
