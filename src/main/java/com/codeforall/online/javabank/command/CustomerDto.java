package com.codeforall.online.javabank.command;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;

/**
 * A concrete implementation of a simple Customer Data Transfer Object to use with the Rest API and with web controllers
 */
public class CustomerDto extends BaseCustomerDto {

    // This annotation include nested object properties directly in the parent object's JSON representation
    @Valid
    @JsonUnwrapped
    private AddressDto addressDto;

    /**
     * Get the address data transfer object
     * @return the address data transfer object
     */
    public AddressDto getAddressDto() {
        return addressDto;
    }

    /**
     * Set the address data transfer object
     * @param addressDto to set
     */
    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }
}
