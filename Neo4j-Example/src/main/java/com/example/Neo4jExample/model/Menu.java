package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class Menu {
    private Collection<FoodSection> foodSections;
    private int price;

    public Menu() {
        this.foodSections = new ArrayList<>();
    }
}
