package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.ProfileCustomerDto;
import com.codeforall.online.javabank.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation that converts {@link Customer} objects to {@link ProfileCustomerDto} objects.
 */
@Component
public class CustomerToProfileCustomerDto implements Converter<Customer, ProfileCustomerDto> {

    private AddressToAddressDto addressToAddressDto;

    /**
     * @see Converter#convert(Object)
     */
    @Override
    public ProfileCustomerDto convert(Customer customer) {

        ProfileCustomerDto profileCustomerDto = new ProfileCustomerDto();

        CustomerMapper.map(customer, profileCustomerDto);
        profileCustomerDto.setAddressDto(addressToAddressDto.convert(customer.getAddress()));
        profileCustomerDto.setPhotoURL(customer.getPhotoURL());
        profileCustomerDto.setNumOfAccounts(customer.getAccounts().size());
        profileCustomerDto.setTotalBalance(customer.getTotalBalance());

        return profileCustomerDto;
    }

    /**
     * Set the addressToAddressDto converter
     * @param addressToAddressDto to set
     */
    @Autowired
    public void setAddressToAddressDto(AddressToAddressDto addressToAddressDto) {
        this.addressToAddressDto = addressToAddressDto;
    }
}
