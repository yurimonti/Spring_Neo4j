package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.Contact;
import lombok.Data;

@Data
public class ContactDTO {
    private Long id;
    private String email;
    private String cellNumber;
    private String fax;

    public ContactDTO(Contact contact){
        this.id = contact.getId();
        this.cellNumber = contact.getCellNumber();
        this.email = contact.getEmail();
        this.fax = contact.getFax();
    }
}
