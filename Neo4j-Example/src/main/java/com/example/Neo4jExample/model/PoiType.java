package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a type of point of interest
 */
@Node
@NoArgsConstructor
@Data
public class PoiType {
    @Id @GeneratedValue
    private Long id;
    private String name;
    @Relationship(type = "TYPE_HAS_CATEGORY",direction = Relationship.Direction.OUTGOING)
    private Collection<CategoryNode> categories;

    @Relationship(type = "TYPE_HAS_TAG",direction = Relationship.Direction.OUTGOING)
    private Collection<TagNode> tags;

    public PoiType(String name) {
        this.name = name;
        this.categories= new ArrayList<>();
        this.tags= new ArrayList<>();
    }

/*    public PoiType(String name, CategoryNode ...categories) {
        this(name);
        this.categories.addAll(Arrays.asList(categories));
    }*/
}
