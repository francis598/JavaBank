package com.codeforall.online.javabank.converters;


import com.codeforall.online.javabank.command.ListCustomerDto;
import com.codeforall.online.javabank.model.Customer;

import org.springframework.stereotype.Component;

/**
 * A concrete converter class that transforms an {@link Customer} entity into an {@link ListCustomerDto}.
 */
@Component
public class CustomerToListCustomerDto extends AbstractConverter<Customer, ListCustomerDto> {

    /**
     * Convert customerDto into customer
     * @param customer to take the info out of
     * @return the listCustomerDto
     */
    @Override
    public ListCustomerDto convert(Customer customer) {

        ListCustomerDto listCustomerDto = new ListCustomerDto();

        CustomerMapper.map(customer, listCustomerDto);
        listCustomerDto.setPhotoURL(customer.getPhotoURL());
        listCustomerDto.setNumOfAccounts(customer.getAccounts().size());
        listCustomerDto.setTotalBalance(customer.getTotalBalance());

        return listCustomerDto;
    }
}
