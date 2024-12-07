package com.codeforall.online.javabank.command;

import com.codeforall.online.javabank.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * A class which represent the data transfer object related to {@link Address}.
 */
public class AddressDto {

    @NotNull(message = "Street is mandatory")
    @NotBlank(message = "Street is mandatory")
    @Size(min = 3, max = 64)
    private String street;

    @NotNull(message = "City is mandatory")
    @NotBlank(message = "City is mandatory")
    @Size(min = 3, max = 64)
    private String city;

    @NotNull(message = "Zip code is mandatory")
    @NotBlank(message = "Zip code is mandatory")
    @Pattern(regexp = "^[1-9]\\d{3}-?\\d{3}$", message = "Zip code contains invalid characters")
    private String zipCode;

    /**
     * Get the street
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Get the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the zipcode
     * @return the zipcode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Set the street to the given street
     * @param street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Set the city to the given city
     * @param city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Set the zip code to the given zip code
     * @param zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
