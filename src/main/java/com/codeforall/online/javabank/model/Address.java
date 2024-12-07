package com.codeforall.online.javabank.model;

import jakarta.persistence.Embeddable;

/**
 * A class which represents a customer's address
 */
@Embeddable
public class Address {

    private String street;
    private String city;
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
