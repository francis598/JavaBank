package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.CustomerDto;
import com.codeforall.online.javabank.exceptions.CustomerNotFoundException;
import com.codeforall.online.javabank.model.Customer;
import com.codeforall.online.javabank.services.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  A converter class which converts {@link CustomerDto} objects into {@link Customer} objects
 */
@Component
public class CustomerDtoToCustomer {

    private CustomerService customerService;
    private AddressDtoToAddress addressDtoToAddress;

    /**
     * Convert a customerDto into a customer
     * @param customerDto to take the info out of
     * @return the customer
     * @throws CustomerNotFoundException if the customer is not found
     */
    public Customer convert(CustomerDto customerDto) throws CustomerNotFoundException {

        Customer customer = (customerDto.getId() != null ? customerService.get(customerDto.getId()) : new Customer());

            customer.setFirstName(customerDto.getFirstName());
            customer.setLastName(customerDto.getLastName());
            customer.setEmail(customerDto.getEmail());
            customer.setPhone(customerDto.getPhone());

            if(customerDto.getId() == null) {
                customer.setPhotoURL("profile-icon.png");
            }

            customer.setAddress(addressDtoToAddress.convert(customerDto.getAddressDto()));

            return customer;
    }

    /**
     * Set the customer service
     * @param customerService to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Set the addressDtoToAddress converter
     * @param addressDtoToAddress to set
     */
    @Autowired
    public void setAddressDtoToAddress(AddressDtoToAddress addressDtoToAddress) {
        this.addressDtoToAddress = addressDtoToAddress;
    }
}

