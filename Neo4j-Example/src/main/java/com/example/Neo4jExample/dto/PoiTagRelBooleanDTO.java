package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.PoiTagRel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoiTagRelBooleanDTO {
    private Long id;
    private TagDTO tag;
    private Boolean value;

    public PoiTagRelBooleanDTO(PoiTagRel poiTagRel){
        this.id = poiTagRel.getId();
        this.tag=new TagDTO(poiTagRel.getTag());
        this.value = poiTagRel.getBooleanValue();
    }
}
