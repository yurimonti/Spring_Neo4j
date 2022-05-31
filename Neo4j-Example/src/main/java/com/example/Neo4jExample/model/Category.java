package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
@Node
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @Relationship(type = "CATEGORY_HAS_TAG",direction = Relationship.Direction.OUTGOING)
    private Collection<Tag> tag;

    @Relationship(type = "SUBCATEGORY_OF",direction = Relationship.Direction.OUTGOING)
    private Collection<MacroCategory> macroCategories;

    private Collection<String> tagString;
    private Collection<String> tagBool;

    public Category(String name) {
        this();
        this.name = name;
        this.tag = new ArrayList<>();
        this.tagString = new ArrayList<>();
        this.tagBool = new ArrayList<>();
        this.macroCategories = new ArrayList<>();
    }
}
