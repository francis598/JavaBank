package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.AddressDto;
import com.codeforall.online.javabank.model.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation that converts {@link AddressDto} objects to {@link Address} objects.
 */
@Component
public class AddressDtoToAddress implements Converter<AddressDto, Address> {

    /**
     * @see Converter#convert(Object)
     */
    @Override
    public Address convert(AddressDto addressDto) {

        Address address = new Address();

        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setZipCode(addressDto.getZipCode());

        return address;
    }
}
