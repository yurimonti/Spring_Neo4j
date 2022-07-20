package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.TagNode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a data transfer object for the class TagNode
 */
@Data
@NoArgsConstructor
public class TagDTO {
    private String name;
    private Boolean isBooleanType;

    public TagDTO(String name, boolean isBooleanType) {
        this.name = name;
        this.isBooleanType = isBooleanType;
    }

    public TagDTO(TagNode tagNode){
        this.name = tagNode.getName();
        this.isBooleanType = tagNode.getIsBooleanType();
    }
}
