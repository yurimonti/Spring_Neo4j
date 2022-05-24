package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.UUID;
//TODO: classe di prova per una entita di relazione.
@RelationshipProperties @Data
public class ManagesThis {

    @RelationshipId
    private Long id;
    @TargetNode
    private City city;

    @Property(name = "prova_ente_city")
    private String prova;

    public ManagesThis() {
    }

    public ManagesThis(City city,String prova) {
        this();
        this.city = city;
        this.prova=prova;
    }
}
