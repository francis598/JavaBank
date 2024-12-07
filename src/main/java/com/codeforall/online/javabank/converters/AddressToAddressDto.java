package com.codeforall.online.javabank.converters;

import com.codeforall.online.javabank.command.AddressDto;
import com.codeforall.online.javabank.model.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation that converts {@link Address} objects to {@link AddressDto} objects.
 */
@Component
public class AddressToAddressDto implements Converter<Address, AddressDto> {

    /**
     * @see Converter#convert(Object)
     */
    @Override
    public AddressDto convert(Address address) {

        AddressDto addressDto = new AddressDto();

        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setZipCode(address.getZipCode());

        return addressDto;
    }
}
