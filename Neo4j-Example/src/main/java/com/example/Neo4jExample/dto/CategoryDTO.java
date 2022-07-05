package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.CategoryNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private String name;

    public CategoryDTO(CategoryNode categoryNode) {
        this.name = categoryNode.getName();
    }
}
