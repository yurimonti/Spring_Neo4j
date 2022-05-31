package com.example.Neo4jExample.model2;

import com.example.Neo4jExample.model.Dish;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class FoodSectionNode {
    private String name;
    private Collection<DishNode> dishes;

    public FoodSectionNode(String name) {
        this.name = name;
        this.dishes = new ArrayList<>();
    }
}