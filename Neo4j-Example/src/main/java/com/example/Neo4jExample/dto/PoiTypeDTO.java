package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.PoiType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class PoiTypeDTO {
    private String name;
    private Collection<CategoryDTO> categories;
    private Collection<TagDTO> tags;

    public PoiTypeDTO(PoiType poiType) {
        this.name = poiType.getName();
        this.categories = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.categories.addAll(poiType.getCategories().stream().map(CategoryDTO::new).toList());
        this.tags.addAll(poiType.getTags().stream().map(TagDTO::new).toList());
    }
}
