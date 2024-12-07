package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.CustomerDto;
import com.codeforall.online.javabank.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A concrete converter class that transforms an {@link Customer} entity into an {@link CustomerDto}.
 */
@Component
public class CustomerToCustomerDto extends AbstractConverter<Customer, CustomerDto> {

    private AddressToAddressDto addressToAddressDto;

    /**
     * Convert a customer into a restCustomerDto
     * @param customer to take info out of
     * @return the restCustomerDto
     */
    @Override
    public CustomerDto convert(Customer customer) {

        CustomerDto customerDto = new CustomerDto();

        CustomerMapper.map(customer, customerDto);

        customerDto.setAddressDto(addressToAddressDto.convert(customer.getAddress()));

        return customerDto;
    }

    /**
     * Set the address data transfer object
     * @param addressToAddressDto to set
     */
    @Autowired
    public void setAddressToAddressDto(AddressToAddressDto addressToAddressDto) {
        this.addressToAddressDto = addressToAddressDto;
    }
}
