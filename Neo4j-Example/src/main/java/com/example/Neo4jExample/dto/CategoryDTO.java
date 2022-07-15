package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.CategoryNode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a data transfer object for the class CategoryNode
 */
@Data
@NoArgsConstructor
public class CategoryDTO {
    private String name;

    public CategoryDTO(CategoryNode categoryNode) {
        this.name = categoryNode.getName();
    }
}
