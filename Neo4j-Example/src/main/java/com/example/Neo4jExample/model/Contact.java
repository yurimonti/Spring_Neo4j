package com.example.Neo4jExample.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Represents a set of contacts
 */
@Data
@NoArgsConstructor
@Node
public class Contact {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String cellNumber;
    private String fax;

    public Contact(String email, String cellNumber, String fax) {
        this();
        this.email = email;
        this.cellNumber = cellNumber;
        this.fax = fax;
    }
}
