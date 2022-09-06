package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.Address;
import lombok.Data;

/**
 * Represents a data transfer object for the class Address
 */
@Data
public class AddressDTO {
    private Long id;
    private String street;
    private Integer number;

    public AddressDTO() {
        this.id = 0L;
        this.street = "";;
        this.number = 0;
    }
    public AddressDTO(Address address){
        this.id = address.getId();
        this.street = address.getStreet();;
        this.number = address.getNumber();
    }
}
