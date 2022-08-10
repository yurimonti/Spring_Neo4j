package com.example.Neo4jExample.dto;

import com.example.Neo4jExample.model.Address;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private Integer number;

    public AddressDTO(Address address){
        this.id = address.getId();
        this.street = address.getStreet();;
        this.number = address.getNumber();
    }
}
