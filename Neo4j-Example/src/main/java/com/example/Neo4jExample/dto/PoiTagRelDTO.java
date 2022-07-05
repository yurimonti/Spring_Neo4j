package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.PoiTagRel;
import com.example.Neo4jExample.model.TagNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
@Data
@NoArgsConstructor
public class PoiTagRelDTO {
    private Long id;
    private TagDTO tag;
    private Boolean booleanValue;
    private String stringValue;

    public PoiTagRelDTO(PoiTagRel poiTagRel){
        this.id = poiTagRel.getId();
        this.tag=new TagDTO(poiTagRel.getTag());
        this.booleanValue = poiTagRel.getBooleanValue();
        this.stringValue = poiTagRel.getStringValue();
    }
}