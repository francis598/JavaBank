package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.BaseCustomerDto;
import com.codeforall.online.javabank.model.Customer;

/**
 * Utilitarian class to map {@link Customer} to {@link BaseCustomerDto}
 */
public class CustomerMapper {

    /**
     * Map the properties of customer to customerDtp
     * @param customer to get the info out of
     * @param baseCustomerDto to set the values
     */
    public static void map(Customer customer, BaseCustomerDto baseCustomerDto) {

        baseCustomerDto.setId(customer.getId());
        baseCustomerDto.setFirstName(customer.getFirstName());
        baseCustomerDto.setLastName(customer.getLastName());
        baseCustomerDto.setEmail(customer.getEmail());
        baseCustomerDto.setPhone(customer.getPhone());
    }
}
