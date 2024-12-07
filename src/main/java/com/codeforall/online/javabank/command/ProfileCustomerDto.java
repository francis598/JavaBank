package com.codeforall.online.javabank.command;


/**
 * A concrete implementation of a Customer Data Transfer Object to show on profile page
 */
public class ProfileCustomerDto extends ListCustomerDto {

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
