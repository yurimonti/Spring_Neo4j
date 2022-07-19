package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.PoiTagRel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoiTagRelStringDTO {
    private Long id;
    private TagDTO tag;
    private String value;

    public PoiTagRelStringDTO(PoiTagRel poiTagRel){
        this.id = poiTagRel.getId();
        this.tag=new TagDTO(poiTagRel.getTag());
        this.value = poiTagRel.getStringValue();
    }
}
